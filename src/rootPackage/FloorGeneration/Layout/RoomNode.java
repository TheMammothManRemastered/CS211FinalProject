package rootPackage.FloorGeneration.Layout;

import rootPackage.FloorGeneration.Features.Feature;

/**
 * The RoomNode class represents a room on a map, along with the rooms it is connected to.
 *
 * @author William Owens
 * @version 3.0
 */
public class RoomNode {
    private RoomNode[] connectedRooms;
    private Feature roomAsFeature;
    private MyPoint2D coordinates;
    private boolean isSpawn;

    public RoomNode() {
        connectedRooms = new RoomNode[4];
        roomAsFeature = null;
        coordinates = null;
        isSpawn = false;
    }

    public MyPoint2D getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(MyPoint2D coordinates) {
        this.coordinates = coordinates;
    }

    public RoomNode[] getConnectedRooms() {
        return connectedRooms;
    }

    public Feature getRoomAsFeature() {
        return roomAsFeature;
    }

    public void setRoomAsFeature(Feature roomAsFeature) {
        this.roomAsFeature = roomAsFeature;
    }

    public boolean isSpawn() {
        return isSpawn;
    }

    public void setSpawn(boolean spawn) {
        isSpawn = spawn;
    }

    public void printRoomAndConnections() {
        System.out.println(coordinates);
        for (int i = 0; i < connectedRooms.length; i++) {
            RoomNode room = connectedRooms[i];
            if (room == null) continue;
            System.out.println(new Connection(coordinates, room.coordinates));
        }
    }

}
