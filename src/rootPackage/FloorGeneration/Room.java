package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Features.Door;
import rootPackage.FloorGeneration.Features.Feature;
import rootPackage.FloorGeneration.Layout.MyPoint2D;
import rootPackage.FloorGeneration.Layout.ProtoRoom;

import java.util.ArrayList;

/**
 * A class representing a room of a floor.
 *
 * @author William Owens
 * @version 2.0
 */
public class Room {

    private MyPoint2D coordinates;
    private ArrayList<Feature> features;
    private boolean isDeadEnd;

    public Room(ProtoRoom protoRoom) {
        this.coordinates = protoRoom.getCoordinates();
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public MyPoint2D getCoordinates() {
        return coordinates;
    }

    public boolean isDeadEnd() {
        return isDeadEnd;
    }

    public void setDeadEnd(boolean deadEnd) {
        isDeadEnd = deadEnd;
    }

    public boolean hasDoorInDirection(Direction dir) {
        ArrayList<Door> doors = new ArrayList<>();
        for (Feature feature : features) {
            if (feature.nameContains("door")) {
                doors.add((Door)feature);
            }
        }
        for (Door door : doors) {
            if (door.getDirection().equals(dir)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            return this.getCoordinates().equals(((Room)obj).getCoordinates());
        }
        return false;
    }
}
