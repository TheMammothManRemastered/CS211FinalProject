package rootPackage.Level.FloorGeneration.Templates;

import org.json.simple.*;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.TopLevel.Room;

/**
 * ADT of all room templates, essentially premade rooms that can be applied to pre-existing {@link Room Room}s.
 *
 * @author William Owens
 * @version 1.0
 */
public abstract class RoomTemplate {

    //TODO: the template series of classes can (and should) probably be refactored into json files
    // the template class itself can remain, but specific room templates should be jsons
    // template class should remain solely for applyTo, which will be needed in some form.

    protected Feature[] featuresNeeded;
    protected String roomDescription;

    /**
     * Applies the template's contents to a given room.
     */
    public void applyTo(Room room) {
        //TODO: might be easiser to read if this is refactored to be in Room, accepting a template or template JSON
        room.setExamineText(roomDescription);
        for (Feature child : featuresNeeded) {
            room.addChild(child);
        }
    }
}
