package rootPackage.Level.FloorGeneration.Layout;

import org.json.simple.*;
import rootPackage.Direction;

/**
 * This class is responsible for generating a list of {@link RoomNode RoomNode}s, which can then be processed into a proper Floor later.
 *
 * @author William Owens
 * @version 3.0
 */
public class FloorLayoutGenerator {

    // both of these are used for this class' main method and should not be used elsewhere
    static StringBuilder delaunayStagesSb = new StringBuilder(); // holds the stages of the delaunay triangulation in string form
    static StringBuilder MSTSb = new StringBuilder(); // holds the minimum spanning tree in string form

    private final MinimumSpanningTree mst;

    /**
     * Constructor.
     */
    public FloorLayoutGenerator(MinimumSpanningTree mst) {
        this.mst = mst;
    }

    /**
     * The bulk of this class' logic. Returns an array of roomNodes, basically just the layout of the floor with no features
     * associated with any rooms, nor a spawn point set.
     */
    public RoomNode[] generateFloor() {

        RoomNode[] rooms = new RoomNode[mst.size()];

        for (int i = 0; i < rooms.length; i++) {
            RoomNode room = new RoomNode();
            MyPoint2D coordinatesOfRoom = mst.getPointByIndex(i);
            room.setCoordinates(coordinatesOfRoom);
            rooms[i] = room;
        }
        for (int i = 0; i < rooms.length; i++) {
            RoomNode room = rooms[i];
            MyPoint2D coordinatesOfRoom = room.getCoordinates();
            double[] adjMatrixRow = mst.getMstGraph().getNodeConnectionIndexes(i);
            for (int j = 0; j < adjMatrixRow.length; j++) {
                if (!(adjMatrixRow[j] > 0)) {
                    continue;
                }
                Connection con = new Connection(coordinatesOfRoom, mst.getPointByIndex(j));
                Direction dir = Direction.degreesToDirection(con.getRelativeDegrees());
                switch (dir) {
                    // connected rooms layout [north, south, east, west]
                    case NORTH -> {
                        room.getConnectedRooms()[0] = rooms[j];
                    }
                    case SOUTH -> {
                        room.getConnectedRooms()[1] = rooms[j];
                    }
                    case EAST -> {
                        room.getConnectedRooms()[2] = rooms[j];
                    }
                    case WEST -> {
                        room.getConnectedRooms()[3] = rooms[j];
                    }
                    default -> {
                    }
                }
            }
        }

        return rooms;

    }


}
