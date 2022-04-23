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
                //TODO: This is catastrophically bad, don't do this. Someone needs to fix this!
                // this code "works", but I think a light breeze would easily break it.
                // the point of this is to establish which side of the room a hallway will go out from, and to which side it leads in the other room
                // remake:
                //      determine angles of every connection branching from the original room
                //      convert all of them to directions
                //      if all of them work out to begin with (no duplicate directions), no further processing need be done
                //      if there are duplicates, the fun begins
                //      get a list of every door the room needs
                //      get a list of every direction the room has already used and add them to a blacklist
                //      for each door, generate a priority of directions it can be on.
                //          first, change the angle of the hallway connection into a polar point 1 unit away from the room's centerpoint
                //          then, generate connections between this new point and each open point in the room (ie. if the north is open, that point would be (room.x, room.y + offset))
                //          sort these connections (lowest weight first).
                //          convert the connections back into directions, and then we have a priority list
                //      now, for each door, select the first available direction in its priority list that is not present in the blacklist
                //      take that direction, add it to the blacklist.
                //      for the room the door connects to on the other side, do a similar priority direction check for that room's open sides
                //      create a door from the selected direction to the other room's highest priority direction
                //      repeat for every door in the queue
                //      repeat for every room
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
