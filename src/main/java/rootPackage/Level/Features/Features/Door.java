package rootPackage.Level.Features.Features;

import rootPackage.Direction;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;
import rootPackage.Main;
import rootPackage.Player;

import java.util.ArrayList;

public class Door extends Feature {

    private RoomNode connectedRoom;
    private boolean locked;
    private Direction positionOnWall;
    private String nameOfKey;
    private String auxText;

    public Door() {
        this.connectedRoom = null;
        this.locked = false;
        this.positionOnWall = null;
        this.flags.add(FeatureFlag.DOOR);
        this.examineText = "";
    }

    public RoomNode getConnectedRoom() {
        return connectedRoom;
    }

    public void setConnectedRoom(RoomNode connectedRoom) {
        this.connectedRoom = connectedRoom;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getNameOfKey() {
        return nameOfKey;
    }

    public void setNameOfKey(String nameOfKey) {
        this.nameOfKey = nameOfKey;
    }

    public boolean tryUnlock(Player player) {
        return (player.getPlayerAsFeature().getChildWithName(nameOfKey) != null);
    }

    public Direction getPositionOnWall() {
        return positionOnWall;
    }

    public void setPositionOnWall(Direction positionOnWall) {
        this.positionOnWall = positionOnWall;
        resetSpriteAndNames();
    }

    public Door getOtherSide() {
        ArrayList<Feature> connectedRoomDoors = connectedRoom.getRoomAsFeature().getChildren(FeatureFlag.DOOR);
        for (Feature feature : connectedRoomDoors) {
            Door door = (Door) feature;
            if (door.getConnectedRoom().getRoomAsFeature().equals(this.parent)) {
                return door;
            }
        }
        return null;
    }

    public Direction getPositionOnWallOfOtherSide() {
        return getOtherSide().getPositionOnWall();
    }

    public void resetSpriteAndNames() {
        switch (positionOnWall) {
            case NORTH -> {
                this.primaryName = "Northern Door";
                this.allNames = new String[]{"north"};
                this.examineText = "There is a door to your north.";
                setSprite(new Sprite(RenderLayer.DOORS, "doorNorth.png"));
            }
            case WEST -> {
                this.primaryName = "Western Door";
                this.allNames = new String[]{"west"};
                this.examineText = "There is a door to your west.";
                setSprite(new Sprite(RenderLayer.DOORS, "doorWest.png"));
            }
            case EAST -> {
                this.primaryName = "Eastern Door";
                this.allNames = new String[]{"east"};
                this.examineText = "There is a door to your east.";
                setSprite(new Sprite(RenderLayer.DOORS, "doorEast.png"));
            }
            default -> {
                this.primaryName = "Southern Door";
                this.allNames = new String[]{"south"};
                this.examineText = "There is a door to your south.";
                setSprite(new Sprite(RenderLayer.NOT_DRAWN, null));
            }
        }
    }

    public String getAuxText() {
        return auxText;
    }

    public void setAuxText(String auxText) {
        this.auxText = auxText;
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                String affix = (isLocked()) ? "It's locked tight. You'll need some sort of key before trying to proceed." : "";
                Main.mainWindow.getConsoleWindow().addEntryToHistory("A door. %s".formatted(affix));
                Main.mainWindow.getConsoleWindow().addEntryToHistory(auxText);
            }
            case UNLOCK -> {
                boolean unlockAttempt = tryUnlock(Main.player);
                if (locked && unlockAttempt) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("The door makes an audible click. Looks like you've unlocked it!");
                    locked = false;
                    Main.player.getPlayerAsFeature().getChildWithName(nameOfKey).removeFromPlay();
                    return;
                }
                if (isLocked()) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("The door stays locked. Seems you don't have the right item to open it...");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The door is already unlocked!");
            }
            case USE -> {
                if (isLocked()) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("Try as you might, the door remains locked shut. Maybe you should unlock it first.");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You open the door and move forwards into the next room.");
                Main.player.setCurrentRoom(connectedRoom);
            }
            default -> {
                onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP, EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Even if you could get this door out of its frame, you're not sure if you could carry it very far.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You're not carrying the door!");
            }
            case ATTACK -> {
                if (isLocked()) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("Seems like hitting the door isn't helping; it's just as locked as it was before");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You strike the door with force, and it slams open! You walk on through and immediately get slapped in the face as the door swings back. Maybe you shouldn't have hit it like that...");
            }
        }
    }
}
