package rootPackage.Level.Features.Equipment;

import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

/**
 * A {@link rootPackage.Level.Features.Feature Feature} representing an accessory.
 * <p></p>
 * Accessories can have any effect, such as a bonus to HP or a higher attack stat when fighting beasts.
 *
 * @version 1.0
 * @author William Owens
 */
public class AccessoryFeature extends Feature {

    public AccessoryFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.EQUIPPABLE);
        flags.add(FeatureFlag.ACCESSORY);
    }

    @Override
    public void react(PlayerAction playerAction) {
        Feature playerAsFeature = Main.player.getPlayerAsFeature();
        ConsoleWindow consoleWindow = Main.mainWindow.getConsoleWindow();
        switch (playerAction) {
            case USE, EQUIP, PICKUP -> {
                // unlike armor, weapons or shields, the player is not limited on the number of accessories they can wear
                this.reparentSelf(playerAsFeature);
                consoleWindow.addEntryToHistory("You equip the %s. %s".formatted(this.getPrimaryName(), "placeholder")); //TODO: load armor stat from json
            }
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("placeholder examine text, this should be loaded from json");
            }
            default -> {
                this.onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The %s has no keyhole of any kind! What are you playing at?".formatted(primaryName));
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You probably shouldn't strike this; breaking it would be unfortunate.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("If you want to keep living in here, you'd best not lose your equipment...");
            }
        }
    }
}

