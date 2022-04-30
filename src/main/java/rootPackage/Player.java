package rootPackage;

import rootPackage.Level.Features.TopLevel.PlayerFeature;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;

/**
 * Represents the player. Currently only holds the player's location.
 *
 * @author William Owens
 * @version 0.2
 */
public class Player {

    private RoomNode currentRoom;
    private PlayerFeature playerAsFeature;

    public PlayerFeature getPlayerAsFeature() {
        return playerAsFeature;
    }

    public void setPlayerAsFeature(PlayerFeature playerAsFeature) {
        this.playerAsFeature = playerAsFeature;
    }

    public Player() {
        currentRoom = null;
        playerAsFeature = null;
    }

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
