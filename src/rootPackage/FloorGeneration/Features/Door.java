package rootPackage.FloorGeneration.Features;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Room;
import rootPackage.Player;

public class Door extends Feature{

    private static final String[] names = new String[] {"door","doorway","portal","gate","entryway","exit"};

    private boolean locked;
    private Door otherSide;

    public Door(String locationInRoom, Room associatedRoom) {
        super(names, locationInRoom, associatedRoom);
    }

    public Door(String locationInRoom) {
        super(names, locationInRoom);
    }

    public Door(String[] names, String locationInRoom, Room associatedRoom) {
        super(names, locationInRoom, associatedRoom);
        locked = false;
    }

    public Door(String[] names, String locationInRoom) {
        super(names, locationInRoom);
        locked = false;
    }

    public Door getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(Door otherSide) {
        this.otherSide = otherSide;
    }

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

    public void unlock() {
        this.locked = false;
        otherSide.setLocked(false);
    }

    //TODO: very, very not done yet. overhaul these (and the stuff in Feature) when the interface system is finalized
    @Override
    public void onExamine(Player player) {
        System.out.print("A ");
        if (locked) {
            System.out.print("locked ");
        }
        System.out.print("door.");
    }

    @Override
    public void onInteract(Player player) {
        if (locked) {
            System.out.println("Try as you might, the door remains locked tight.");
        }
        else {
            System.out.println("You slip through the door, and, seemingly on its own, it creaks shut behind you.");
            player.setCurrentRoom(otherSide.getAssociatedRoom());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Door) {
            Door other = (Door)obj;
            return other.getAssociatedRoom().equals(this.getAssociatedRoom()) && other.getOtherSide().equals(this.getOtherSide());
        }
        return false;
    }
}
