

public class Door implements Feature {

    private Room connectedRoom;
    private FeatureLocation location;

    public Door(Room connectedRoom, FeatureLocation location) {
        this.connectedRoom = connectedRoom;
        this.location = location;
    }

    public Room getConnectedRoom() {
        return connectedRoom;
    }

    public FeatureLocation getLocation() {
        return location;
    }
}
