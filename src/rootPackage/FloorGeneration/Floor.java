package rootPackage.FloorGeneration;

import java.util.ArrayList;

/**
 * Represents a floor. Currently only holds rooms.
 *
 * @author William Owens
 * @version 1.0
 */
public class Floor {

    private ArrayList<Room> rooms;

    public Floor(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
