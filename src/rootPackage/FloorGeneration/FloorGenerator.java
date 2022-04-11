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

    public FloorGenerator(FloorLayout layout) {
        this.layout = layout;
    }

    /**
     * Generates the floor. If there is no layout set already (which will usually be the case) one will be generated here.
     * @return
     */
    public Floor generateFloor() {

        // generate the layout
        if (this.layout == null) {
            // seeded demonstration of level gen (10 points between (1,1) and (10,10), seed 44)
            Random rng = new Random(44);
            Point2D.Double[] inputs = new Point2D.Double[10];
            for (int i = 0; i < 10; i++) {
                Point2D.Double point = new Point2D.Double(rng.nextInt(10) + 1, rng.nextInt(10) + 1);
                boolean exist = false;
                for (Point2D.Double pointInArray : inputs) {
                    if (point.equals(pointInArray)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    inputs[i] = point;
                    continue;
                }
                i--;

            }


            DelaunayTriangulation del = new DelaunayTriangulation(inputs);
            ArrayList[] triangulation = del.triangulate(); // triangulate the inputs

            MSTMaker mst = new MSTMaker(triangulation);
            MinimumSpanningTree minimumSpanningTree = mst.generateMST();

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
        //TODO: again, pretty sure all random should be standardized, make sure that's the case later along
        Random rng = new Random(44);
        int spawnIndex = rng.nextInt(rooms.size());
        rooms.get(spawnIndex).setSpawnRoom(true);

        return new Floor(rooms);
    }


}
