package rootPackage;

import rootPackage.FloorGeneration.Room;

/**
 * Represents the player. Currently only holds the player's location.
 *
 * @author William Owens
 * @version 0.1
 */
public class Player {

    private Room currentRoom;

    public Player(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
