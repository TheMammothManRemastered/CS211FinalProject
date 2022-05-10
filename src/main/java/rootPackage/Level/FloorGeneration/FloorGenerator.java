package rootPackage.Level.FloorGeneration;

import rootPackage.Direction;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Features.Door;
import rootPackage.Level.Features.Features.HealthPoint;
import rootPackage.Level.Features.Features.Moss;
import rootPackage.Level.Features.TopLevel.Room;
import rootPackage.Level.Floor;
import rootPackage.Level.FloorGeneration.Layout.*;
import rootPackage.Level.FloorGeneration.Templates.RoomAlias;
import rootPackage.Level.FloorGeneration.Templates.Templates.IntraFloorShop;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The floor generator class is responsible for generating the Floor objects for each level.
 *
 * @author William Owens
 * @version 2.8
 */
public class FloorGenerator {

    /**
     * Generates a floor using the given theme.
     */
    public static Floor generateFloor(FloorTheme theme) {
        //TODO: not the cleanest code in the world, refactor this so it's broken into more private methods.

        // generate the layout
        // first, generate a valid MST with no sharp connections
        boolean validMST = false;
        MinimumSpanningTree minimumSpanningTree = null;
        Triangulation triangulation = null;
        while (!validMST) {
            int sizeOfFloor = theme.getMinimumSize() + FloorGenerationRNG.rng.nextInt(theme.getMaximumSize() - theme.getMinimumSize()) + 1;
            Point2D.Double[] inputs = new Point2D.Double[sizeOfFloor];
            for (int i = 0; i < sizeOfFloor; i++) {
                Point2D.Double point = new Point2D.Double(FloorGenerationRNG.rng.nextInt(50) + 1, FloorGenerationRNG.rng.nextInt(50) + 1);
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
            triangulation = del.triangulate(); // triangulate the inputs

            MSTMaker mst = new MSTMaker(triangulation);
            minimumSpanningTree = mst.generateMST();

            validMST = (minimumSpanningTree.getDeadEnds().length >= theme.getDeadEndsNeeded()) && minimumSpanningTree.isMSTValid();
        }

        // now, add random connections from the delaunay triangulation to anywhere other than dead ends
        MyPoint2D[] deadEnds = minimumSpanningTree.getDeadEnds();
        ArrayList<Connection> connectionsInDelaunayTriangulation = triangulation.getConnections();
        ArrayList<Connection> connectionsNotAttachedToDeadEnds = new ArrayList<>();
        // get all the connections from the original triangulation that are not connected to any dead end
        for (Connection connection : connectionsInDelaunayTriangulation) {
            if (!connection.containsAny(deadEnds)) {
                connectionsNotAttachedToDeadEnds.add(connection);
            }
        }
        // remove elements already contained in the minimum spanning tree
        for (Connection connection : minimumSpanningTree.getConnections()) {
            connectionsNotAttachedToDeadEnds.remove(connection);
            connectionsNotAttachedToDeadEnds.remove(connection.opposite());
        }

        // delete duplicate elements
        for (int i = 0; i < connectionsNotAttachedToDeadEnds.size(); i++) {
            Connection connection = connectionsNotAttachedToDeadEnds.get(i);
            connectionsNotAttachedToDeadEnds.remove(connection.opposite());
        }

        //TODO: mst generation is fine as is, good QOL would be to add in old connections to make it a bit less 'snakey'

        // generate the floor's layout
        FloorLayoutGenerator flg = new FloorLayoutGenerator(minimumSpanningTree);
        RoomNode[] layout = flg.generateFloor();
        Floor floor = new Floor(new ArrayList<>(Arrays.asList(layout)));

        // attach roomAsFeatures to each node
        for (RoomNode roomNode : floor.getRooms()) {
            Room room = new Room();
            roomNode.setRoomAsFeature(room);
        }

        // set up doors
        for (RoomNode roomNode : floor.getRooms()) {
            Feature room = roomNode.getRoomAsFeature();
            RoomNode[] connectedRoomNodes = roomNode.getConnectedRooms();
            for (int i = 0; i < 4; i++) {
                if (connectedRoomNodes[i] != null) {
                    Door door = new Door();
                    door.setConnectedRoom(connectedRoomNodes[i]);
                    door.setPositionOnWall(Direction.numToDirection(i));
                    room.addChild(door);
                    door.resetSpriteAndNames();
                }
            }
        }

        // get dead end roomnodes and others
        ArrayList<RoomNode> deadEndNodes = new ArrayList<>();
        ArrayList<RoomNode> nonDeadEndNodes = new ArrayList<>();
        for (RoomNode roomNode : floor.getRooms()) {
            if (roomNode.getNumNeighbors() == 1) {
                deadEndNodes.add(roomNode);
                continue;
            }
            nonDeadEndNodes.add(roomNode);
        }

        // set spawnpoint
        int indexOfSpawn = FloorGenerationRNG.rng.nextInt(nonDeadEndNodes.size());
        nonDeadEndNodes.get(indexOfSpawn).setSpawn(true);

        // pick a room to hold the boss
        ArrayList<Integer> deadEndsUsed = new ArrayList<>();
        int bossRoomIndex = FloorGenerationRNG.rng.nextInt(deadEndNodes.size());
        deadEndsUsed.add(bossRoomIndex);
        RoomNode bossRoomNode = deadEndNodes.get(bossRoomIndex);
        theme.getExitRoomFeatures().applyTo((Room) bossRoomNode.getRoomAsFeature());
        ((Door) bossRoomNode.getRoomAsFeature().getChildren(FeatureFlag.DOOR).get(0)).getOtherSide().setLocked(true);
        ((Door) bossRoomNode.getRoomAsFeature().getChildren(FeatureFlag.DOOR).get(0)).getOtherSide().setNameOfKey(theme.getKeyName());
        ((Door) bossRoomNode.getRoomAsFeature().getChildren(FeatureFlag.DOOR).get(0)).getOtherSide().setAuxText(theme.getBossDoorDescription());

        // pick a room to hold the key, whatever that might be
        int keyRoomIndex = FloorGenerationRNG.rng.nextInt(deadEndNodes.size());
        while (keyRoomIndex == bossRoomIndex) {
            keyRoomIndex = FloorGenerationRNG.rng.nextInt(deadEndNodes.size());
        }
        deadEndsUsed.add(keyRoomIndex);
        RoomNode keyRoomNode = deadEndNodes.get(keyRoomIndex);
        theme.getKeyRoomFeatures().applyTo((Room) keyRoomNode.getRoomAsFeature());

        // if there are any remaining dead ends, put stuff in them
        if (deadEndsUsed.size() != deadEndNodes.size()) {
            //it's a priority to always have a treasure room, otherwise the player may be stuck with starting gear
            int indexOfTreasureRoom = FloorGenerationRNG.rng.nextInt(deadEndNodes.size());
            while (deadEndsUsed.contains(indexOfTreasureRoom)) {
                indexOfTreasureRoom = FloorGenerationRNG.rng.nextInt(deadEndNodes.size());
            }
            deadEndsUsed.add(indexOfTreasureRoom);
            RoomAlias.getTemplate("treasureRoomGeneric").applyTo((Room) deadEndNodes.get(indexOfTreasureRoom).getRoomAsFeature());
        }

        // now, start picking stuff from the theme
        //if (deadEndsUsed.size() != deadEndNodes.size()) {
        //it's a priority to always have a treasure room, otherwise the player may be stuck with starting gear
        //int indexOfTreasureRoom = FloorGenerationRNG.rng.nextInt(deadEndsUsed.size());
        //while (deadEndsUsed.contains(indexOfTreasureRoom)) {
        //    indexOfTreasureRoom = FloorGenerationRNG.rng.nextInt(deadEndsUsed.size());
        //}
        //deadEndsUsed.add(indexOfTreasureRoom);
        //RoomAlias.getTemplate("treasureRoomGeneric").applyTo((Room) deadEndNodes.get(indexOfTreasureRoom).getRoomAsFeature());
        //}

        // spawn enemies in the non-dead-end rooms
        ArrayList<Integer> used = new ArrayList<>();
        for (Feature enemy : theme.getEnemies()) {
            int suggestion = FloorGenerationRNG.rng.nextInt(nonDeadEndNodes.size());
            while (used.contains(suggestion)) {
                suggestion = FloorGenerationRNG.rng.nextInt(nonDeadEndNodes.size());
            }
            used.add(suggestion);
            nonDeadEndNodes.get(suggestion).getRoomAsFeature().addChild(enemy);
        }

        // spawn health points in rooms without enemies
        for (int i = 0; i < theme.getHealthPoints(); i++) {
            int suggestion = FloorGenerationRNG.rng.nextInt(nonDeadEndNodes.size());
            while (used.contains(suggestion)) {
                suggestion = FloorGenerationRNG.rng.nextInt(nonDeadEndNodes.size());
            }
            used.add(suggestion);
            nonDeadEndNodes.get(suggestion).getRoomAsFeature().addChild(new HealthPoint());
        }

        // put decorative features in non-dead-end rooms
        for (RoomNode node : nonDeadEndNodes) {
            int roll = FloorGenerationRNG.rng.nextInt(101);
            if (roll > 20) {
                if (roll > 75) {
                    Feature moss = new Moss();
                    node.getRoomAsFeature().addChild(moss);
                } else if (roll > 30) {
                    node.getRoomAsFeature().setSprite(new Sprite(RenderLayer.WALLS, "crack%d.png".formatted(FloorGenerationRNG.rng.nextInt(3) + 1)));
                    node.getRoomAsFeature().setExamineText("The walls in this room are rather badly cracked.");
                }
            }
        }

        return floor;
    }

    /**
     * Creates a shop floor. The shop floor only has one room, which is the same every time.
     */
    public static Floor generateShop() {
        RoomNode room = new RoomNode();
        room.setSpawn(true);
        room.setRoomAsFeature(new Room());
        room.getRoomAsFeature().setSprite(new Sprite(RenderLayer.WALLS, "void.png"));
        room.setCoordinates(new MyPoint2D(25, 25));
        IntraFloorShop ifs = new IntraFloorShop();
        ifs.applyTo((Room) room.getRoomAsFeature());
        return new Floor(new ArrayList<>(Arrays.asList(room)));
    }


}
