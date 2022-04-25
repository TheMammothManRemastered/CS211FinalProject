package rootPackage.FloorGeneration.Features;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Layout.MyPoint2D;
import rootPackage.FloorGeneration.Room;
import rootPackage.Player;

/**
 * Doors are one of, if not the, most important features. They are what enables players to move from room to room.
 *
 * @author William Owens
 * @version 3.0
 */
public class Door extends Feature{

    //TODO: refactor to implement Unlockable

    // names for a door. should anyone else be reading this and know some other words for door, hit me up lol
    private static final String[] names = new String[] {"door","doorway","portal","gate","entryway","exit"};

    private Door otherSide;

    /**
     * Constructor.
     * @param locationInRoom A string representing the location of the door in the room. should be in the form "wall{direction}" (with the {direction} being replaced by a direction in all caps)
     */
    //TODO: change this so capitalization doesn't matter
    public Door(String locationInRoom, Room associatedRoom) {
        super(names, locationInRoom, associatedRoom);
    }

    /**
     * Constructor.
     * @param locationInRoom A string representing the location of the door in the room. should be in the form "wall{direction}" (with the {direction} being replaced by a direction in all caps)
     */
    public Door(String locationInRoom) {
        super(names, locationInRoom);
    }

    public MyPoint2D getCoordinates() {
        MyPoint2D point2D = new MyPoint2D(getAssociatedRoom().getCoordinates());
        Direction dir = this.getDirection();
        switch (dir) {
            case NORTH -> {
                point2D.y-=0.25;
            }
            case SOUTH -> {
                point2D.y+=0.25;
            }
            case EAST -> {
                point2D.x+=0.25;
            }
            case WEST -> {
                point2D.x-=0.25;
            }
            default -> {
            }
        }
        return point2D;
    }

    public Door getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(Door otherSide) {
        this.otherSide = otherSide;
    }

    /**
     * Get the wall on which this door lies.
     */
    public Direction getDirection() {
        String dirString = getLocationInRoom().substring(4);
        switch (dirString) {
            case "NORTH" -> {
                return Direction.NORTH;
            }
            case "SOUTH" -> {
                return Direction.SOUTH;
            }
            case "WEST" -> {
                return Direction.WEST;
            }
            case "EAST" -> {
                return Direction.EAST;
            }
        }
        return Direction.CENTER;
    }

    public boolean tryUnlock(Feature keyBeingUsed) {
        return false;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Door) {
            Door other = (Door)obj;
            return other.getAssociatedRoom().equals(this.getAssociatedRoom()) && other.getOtherSide().equals(this.getOtherSide());
        }
        return false;
    }

    //TODO: temp until door refactor is done
    boolean locked = false;
    public Room getAssociatedRoom() {
        return null;
    }
    public String getLocationInRoom() {
        return null;
    }
}
