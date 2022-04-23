package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Features.Door;
import rootPackage.FloorGeneration.Layout.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The floor generator class is responsible for generating the Floor objects for each level.
 *
 * @author William Owens
 * @version 1.2
 */
public class FloorGenerator {

    private FloorLayout layout;

    public FloorGenerator() {
        layout = null;
    }

    //TODO: make generateFloor take a parameter defining the things the floor has to have (ie. at least 2 dead ends, at least x features, all that. basically the floor's theme)
    /**
     * Generates the floor. If there is no layout set already (which will usually be the case) one will be generated here.
     */
    public Floor generateFloor() {

        // generate the layout
        if (this.layout == null) {
            //TODO: this is currently very seeded and hardcoded
            Point2D.Double[] inputs = new Point2D.Double[10];
            for (int i = 0; i < 10; i++) {
                Point2D.Double point = new Point2D.Double(FloorGenerationRNG.rng.nextInt(10) + 1, FloorGenerationRNG.rng.nextInt(10) + 1);
                boolean exist = false;
                for (Point2D.Double pointInArray : inputs) {
                    if (point.equals(pointInArray)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    inputs[i] = point;
                    continue;
                }
                i--;

            }


            DelaunayTriangulation del = new DelaunayTriangulation(inputs);
            Triangulation triangulation = del.triangulate(); // triangulate the inputs

            MSTMaker mst = new MSTMaker(triangulation);
            MinimumSpanningTree minimumSpanningTree = mst.generateMST();

            //TODO: check the minimumSpanningTree to make sure it has no nodes with more than 4 connections. if it does, redo the entire generation
            // once we happen upon a good MST, grab a random number of random connections from the triangulation that do not connect to any of the MST's dead ends
            // add these connections back into the MST (making sure they don't make nodes with more than 4 connections)
            // now, we can generate the floor using this new graph
            // this functionality should probably be included in the floor layout generator rather than this one tbh

            FloorLayoutGenerator flg = new FloorLayoutGenerator(minimumSpanningTree);

            this.layout = flg.generateFloor();
        }

        // fill the floor with all the relevant rooms
        ArrayList<Room> rooms = new ArrayList<>();
        ProtoRoom[] protoRooms = layout.getRooms();
        for (ProtoRoom protoRoom : protoRooms) {
            Room room = new Room(protoRoom);
            rooms.add(room);
        }

        // now, set up doors between the rooms
        Direction[] directions = Direction.allButCenter();
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            ProtoRoom protoRoom = protoRooms[i];
            for (Direction dir : directions) {
                if (protoRoom.getRoom(dir) == null) {
                    continue;
                }
                if (room.hasDoorInDirection(dir)) {
                    continue;
                }

                //TODO: debug location until this gets standardized
                Door thisSideDoor = new Door("wall"+dir,room);
                // get other room's direction to this one
                ProtoRoom otherProtoRoom = protoRoom.getRoom(dir);
                Direction dirOther = dir.opposite();
                for (Direction direction : directions) {
                    if (otherProtoRoom.getRoom(direction) == null) {
                        continue;
                    }
                    if (otherProtoRoom.getRoom(direction).equals(protoRoom)) {
                        dirOther = direction;
                        break;
                    }
                }
                int otherRoomIndex = List.of(protoRooms).indexOf(otherProtoRoom);
                Door otherSideDoor = new Door("wall"+dirOther,rooms.get(otherRoomIndex));
                thisSideDoor.setOtherSide(otherSideDoor);
                otherSideDoor.setOtherSide(thisSideDoor);
                room.getFeatures().add(thisSideDoor);
                rooms.get(otherRoomIndex).getFeatures().add(otherSideDoor);
            }

        }

        // set one of the rooms to be the spawnpoint
        //TODO: the spawnpoint is random, so long as it's not in a dead end
        //TODO: again, pretty sure all random should be standardized, make sure that's the case later along
        Random rng = new Random(44);
        int spawnIndex = rng.nextInt(rooms.size());
        rooms.get(spawnIndex).setSpawnRoom(true);

        //TODO: flag the furthest dead end from the spawn room as the exit/boss room

        //TODO: get all the required special rooms from the template, and put them in dead ends/whichever room they're meant to be in

        //TODO: for any remaining dead ends, put generic special rooms (small treasure, minor puzzle, etc)

        //TODO: locked doors can only be created leading into special rooms. this avoids making floors unsolvable.
        // for every locked door made, make sure its key or unlocking mechanism is accessible. ie. spawn them somewhere other than the dead ends

        //TODO: for all other rooms on the floor, propagate them with some theme-specific stuff, and random generic stuff (ie. ares' theme has bloodstains and corpses more often than athena's theme, which may have bookshelves or scrolls. Both have torches or rugs)

        return new Floor(rooms);
    }


}
