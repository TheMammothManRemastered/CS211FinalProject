package rootPackage.Level.FloorGeneration.Templates;

import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.TopLevel.Room;

import java.util.ArrayList;

public abstract class RoomTemplate {

    protected Feature[] featuresNeeded;
    protected String roomDescription;

    public Feature[] getFeaturesNeeded() {
        return featuresNeeded;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void applyTo(Room room) {
        room.setExamineText(roomDescription);
        for (Feature child : featuresNeeded) {
            room.addChild(child);
        }
    }
}
