package rootPackage.FloorGeneration;

import rootPackage.FloorGeneration.Layout.RoomNode;

import java.util.ArrayList;

/**
 * Represents a floor. Currently only holds rooms.
 *
 * @author William Owens
 * @version 3.0
 */
public class Floor {

    private ArrayList<RoomNode> rooms;

    public Floor(ArrayList<RoomNode> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<RoomNode> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<RoomNode> rooms) {
        this.rooms = rooms;
    }
}
