package rootPackage.Level.Features.TopLevel;

import rootPackage.Level.Features.Feature;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

/**
 * A class representing a room as a top-level feature.
 * <p></p>
 * NOTE: This class is NOT to be confused with {@link rootPackage.Level.FloorGeneration.Layout.RoomNode RoomNode}!
 * If {@link rootPackage.Level.FloorGeneration.Layout.RoomNode RoomNode} is to the number of an office room, then {@link Room Room} is to that room's contents.
 *
 * @version 1.1
 * @author William Owens
 */
public class Room extends Feature {

    public Room(String primaryName, String[] allNames) {
        super(primaryName, allNames);
    }

    public Room(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        super(primaryName, allNames, parent, children, flags);
    }

    @Override
    public void react(PlayerAction playerAction) {
        if (playerAction == PlayerAction.EXAMINE) {
            Main.mainWindow.getConsoleWindow().addEntryToHistory("placeholder room description");
            if (this.hasChildren()) {
                for (Feature child : children) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory(child.getPrimaryName());
                }
            }
        } else {
            this.onActionNotApplicable(playerAction);
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You can't pick up an entire room!");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You can't drop what you aren't even holding!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The room isn't locked.");
            }
            case USE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("What? How are you using a room? That doesn't make any sense!");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking a room? Not sure that's such a good plan.");
            }
            case EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Wearing an entire room? How would you even do that?");
            }
        }
    }
}
