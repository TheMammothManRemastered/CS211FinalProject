package rootPackage;

import rootPackage.FloorGeneration.Layout.RoomNode;

/**
 * Represents the player. Currently only holds the player's location.
 *
 * @author William Owens
 * @version 0.1
 */
public class Player {

    private RoomNode currentRoom;

    public Player(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
    }

    public RoomNode getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
    }
}
