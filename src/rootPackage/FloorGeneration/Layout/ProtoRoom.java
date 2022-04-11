package rootPackage.FloorGeneration.Layout;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Features.Door;

/**
 * A class representing a room before anything of interest is added to it.
 *
 * @author William
 * @version 1.0
 */
public class ProtoRoom {

    private MyPoint2D coordinates;
    private ProtoRoom roomNorth, roomSouth, roomEast, roomWest;

    public ProtoRoom(MyPoint2D coordinates) {
        this.coordinates = coordinates;
        roomNorth = null;
        roomSouth = null;
        roomEast = null;
        roomWest = null;
    }

    public MyPoint2D getCoordinates() {
        return coordinates;
    }

    public ProtoRoom getRoom(Direction dir) {
        switch (dir) {
            case NORTH -> {
                return this.roomNorth;
            }
            case SOUTH -> {
                return this.roomSouth;
            }
            case EAST -> {
                return this.roomEast;
            }
            case WEST -> {
                return this.roomWest;
            }
            default -> {
                return this;
            }
        }
    }

    public void addRoom(Direction dir, ProtoRoom room) throws RoomAdditionException{
        switch (dir) {
            case NORTH -> {
                if (this.roomNorth == null) {
                    this.roomNorth = room;
                    return;
                }
            }
            case SOUTH -> {
                if (this.roomSouth == null) {
                    this.roomSouth = room;
                    return;
                }
            }
            case EAST -> {
                if (this.roomEast == null) {
                    this.roomEast = room;
                    return;
                }
            }
            case WEST -> {
                if (this.roomWest == null) {
                    this.roomWest = room;
                    return;
                }
            }
            default -> {
            }
        }
        throw new RoomAdditionException("A room already exists in this direction!");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProtoRoom) {
            return this.getCoordinates().equals(((ProtoRoom)obj).getCoordinates());
        }
        return false;
    }
}

class RoomAdditionException extends Exception {

    public RoomAdditionException(String message) {
        super(message);
    }
}