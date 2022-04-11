package rootPackage.FloorGeneration.Layout;

import rootPackage.Direction;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class is responsible for generating a FloorLayout object from a MinimumSpanningTree, which can then be processed into a proper Floor later.
 *
 * @author William Owens
 * @version 1.2
 */
public class FloorLayoutGenerator {

    // both of these are used for this class' main method and should not be used elsewhere
    static StringBuilder delaunayStagesSb = new StringBuilder(); // holds the stages of the delaunay triangulation in string form
    static StringBuilder MSTSb = new StringBuilder(); // holds the minimum spanning tree in string form

    private MinimumSpanningTree mst;
    private int numDeadEnds;

    /**
     * This class' main method exists for demonstrative purposes only, and should not be called under normal operation.
     * @param args
     */
    public static void main(String[] args) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("Input points (numbered)\n");
        for (int i = 0; i < triangulation[0].size(); i++) {
            sb.append(("p_{%d}=").formatted(i));
            sb.append(triangulation[0].get(i).toString());
            sb.append("\n");
        }
        sb.append("\nDelaunay Triangulation\n");
        sb.append(delaunayStagesSb.toString());
        sb.append("\nMinimum Spanning Tree generation\n");
        MSTMaker mst = new MSTMaker(triangulation);
        MinimumSpanningTree minimumSpanningTree = mst.generateMST();
        sb.append(MSTSb);

        System.out.println(sb);

        System.out.println("\n\n\n");

        minimumSpanningTree.printMST();

        System.out.println("\n\n\n\n");

        FloorLayoutGenerator flg = new FloorLayoutGenerator(minimumSpanningTree);

        FloorLayout layout = flg.generateFloor();
        layout.printFloorDesmos();

    }

    /**
     * Constructor.
     */
    public FloorLayoutGenerator(MinimumSpanningTree mst) {
        this.mst = mst;
        this.numDeadEnds = mst.getDeadEnds().length;
    }

    /**
     * The bulk of this class' logic. Returns a FloorLayout made from the MST given to the constructor.
     */
    public FloorLayout generateFloor() {
        // okay logic time
        // rooms are put into a queue and given neighbors based on that

        ArrayList<ProtoRoom> finishedRooms = new ArrayList<>();
        ArrayList<ProtoRoom> roomQueue = new ArrayList<>();

        // add first point in the MST to the queue
        roomQueue.add(new ProtoRoom(mst.getPointByIndex(0)));

        // while the queue isn't empty, iterate
        while (roomQueue.size() > 0) {
            // get first room out of the queue
            ProtoRoom room = roomQueue.get(0);
            roomQueue.remove(0);
            // get arrays of connected rooms and their points, as well as the angles to each of these connections
            MyPoint2D[] connectedPoints = mst.getConnectedPoints(room.getCoordinates());
            ProtoRoom[] connectedRooms = new ProtoRoom[connectedPoints.length];
            double[] angles = new double[connectedPoints.length];
            for (int i = 0; i < connectedPoints.length; i++) {
                Connection con = new Connection(room.getCoordinates(), connectedPoints[i]);
                angles[i] = con.getRelativeDegrees();
                connectedRooms[i] = new ProtoRoom(connectedPoints[i]);
            }

            // this loop attaches connected rooms to the current room from the correct door direction
            for (int i = 0; i < angles.length; i++) {
                double angle = angles[i];
                ProtoRoom connectedRoom = connectedRooms[i];
                Direction dir = Direction.degreesToDirection(angle);
                try {
                    room.addRoom(dir, connectedRoom);
                    if (!(roomQueue.contains(connectedRoom)) && !(finishedRooms.contains(connectedRoom))) {
                        roomQueue.add(connectedRoom);
                    }
                } catch (RoomAdditionException e) {
                    // this catch block executes if there is already a room connected to the door this room wants to use
                    // if this is the case, there is likely another direction open that can handle the connection
                    // this block uses the other direction, should that be the case
                    if (room.getCoordinates().equals(new MyPoint2D(47,41))) {
                        System.out.println("we're on that one point");
                        System.out.println(dir);
                        System.out.println(angle);
                    }
                    boolean b = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
                    boolean c = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);
                    if (angle > 0) {
                        if (b) {
                            if (room.getRoom(Direction.EAST) == null) {
                                try {
                                    room.addRoom(Direction.EAST, connectedRoom);
                                    if (!(roomQueue.contains(connectedRoom)) && !(finishedRooms.contains(connectedRoom))) {
                                        roomQueue.add(connectedRoom);
                                    }
                                } catch (RoomAdditionException ignored){}
                            }
                        }
                        else if (c) {
                            if (room.getRoom(Direction.SOUTH) == null) {
                                try {
                                    room.addRoom(Direction.SOUTH, connectedRoom);
                                    if (!(roomQueue.contains(connectedRoom)) && !(finishedRooms.contains(connectedRoom))) {
                                        roomQueue.add(connectedRoom);
                                    }
                                } catch (RoomAdditionException ignored){}
                            }
                        }
                    }
                    else if (angle < 0) {
                        if (b) {
                            if (room.getRoom(Direction.WEST) == null) {
                                try {
                                    room.addRoom(Direction.WEST, connectedRoom);
                                    if (!(roomQueue.contains(connectedRoom)) && !(finishedRooms.contains(connectedRoom))) {
                                        roomQueue.add(connectedRoom);
                                    }
                                } catch (RoomAdditionException ignored){}
                            }
                        }
                        else if (c)  {
                            if (room.getRoom(Direction.NORTH) == null) {
                                try {
                                    room.addRoom(Direction.NORTH, connectedRoom);
                                    if (!(roomQueue.contains(connectedRoom)) && !(finishedRooms.contains(connectedRoom))) {
                                        roomQueue.add(connectedRoom);
                                    }
                                } catch (RoomAdditionException ignored){}
                            }
                        }
                    }
                }
            }

            finishedRooms.add(room);

        }

        return new FloorLayout(finishedRooms.toArray(new ProtoRoom[finishedRooms.size()]));
    }


}
