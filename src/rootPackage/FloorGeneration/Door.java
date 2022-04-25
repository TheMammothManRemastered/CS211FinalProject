package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Layout.MyPoint2D;

/**
 * A door is part of a room. These are responsible for connecting rooms together.
 *
 * @author William Owens
 * @version 3.0
 */
public class Door {

    // names for a door. should anyone else be reading this and know some other words for door, hit me up lol
    private static final String[] names = new String[] {"door","doorway","portal","gate","entryway","exit"};
    private String locationInRoom;
    private Room associatedRoom;
    private boolean locked;

    private rootPackage.FloorGeneration.Features.Door otherSide;

    /**
     * Constructor.
     * @param locationInRoom A string representing the location of the door in the room. should be in the form "wall{direction}" (with the {direction} being replaced by a direction in all caps)
     */
    //TODO: change this so capitalization doesn't matter
    public Door(String locationInRoom, Room associatedRoom) {
        this.locationInRoom = locationInRoom;
        this.associatedRoom = associatedRoom;
        locked = false;
    }

    /**
     * Constructor.
     * @param locationInRoom A string representing the location of the door in the room. should be in the form "wall{direction}" (with the {direction} being replaced by a direction in all caps)
     */
    public Door(String locationInRoom) {
        this.locationInRoom = locationInRoom;
        this.associatedRoom = null;
        locked = false;
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

    public rootPackage.FloorGeneration.Features.Door getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(rootPackage.FloorGeneration.Features.Door otherSide) {
        this.otherSide = otherSide;
    }

    public void setLocationInRoom(String locationInRoom) {
        this.locationInRoom = locationInRoom;
    }

    public void setAssociatedRoom(Room associatedRoom) {
        this.associatedRoom = associatedRoom;
    }

    public String getLocationInRoom() {
        return locationInRoom;
    }

    public Room getAssociatedRoom() {
        return associatedRoom;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof rootPackage.FloorGeneration.Features.Door) {
            rootPackage.FloorGeneration.Features.Door other = (rootPackage.FloorGeneration.Features.Door)obj;
            return other.getAssociatedRoom().equals(this.getAssociatedRoom()) && other.getOtherSide().equals(this.getOtherSide());
        }
        return false;
    }



}

