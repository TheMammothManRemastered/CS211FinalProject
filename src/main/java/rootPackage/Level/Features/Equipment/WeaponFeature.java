package rootPackage.Level.Features.Equipment;

import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

/**
 * A {@link rootPackage.Level.Features.Feature Feature} representing an equippable weapon.
 * <p></p>
 * Weapons are responsible for setting the player's attack stat.
 *
 * @version 1.0
 * @author William Owens
 */
public class WeaponFeature extends Feature {

    public WeaponFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.WEAPON);
        flags.add(FeatureFlag.EQUIPPABLE);
    }

    @Override
    public void react(PlayerAction playerAction) {
        Feature roomAsFeature = Main.player.getCurrentRoom().getRoomAsFeature();
        Feature playerAsFeature = Main.player.getPlayerAsFeature();
        ConsoleWindow consoleWindow = Main.mainWindow.getConsoleWindow();
        switch (playerAction) {
            case USE, EQUIP, PICKUP -> {
                ArrayList<Feature> playerWeaponFeatures = playerAsFeature.getChildren(FeatureFlag.WEAPON);
                if (playerWeaponFeatures.size() != 0) {
                    // the player has no weapon equipped, proceed normally, otherwise it has to be removed
                    for (Feature weapon : playerWeaponFeatures) {
                        consoleWindow.addEntryToHistory("You gingerly place your %s upon the floor.".formatted(weapon.getPrimaryName()));
                        weapon.reparentSelf(roomAsFeature);
                    }
                }
                this.reparentSelf(playerAsFeature);
                consoleWindow.addEntryToHistory("You now wield the %s. Your attack is now %d".formatted(this.getPrimaryName(), 666666)); //TODO: load damage stat from json
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The weapon has no keyhole of any kind! What are you playing at?");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You attack the %s with your trusty %s... wait a minute...".formatted(primaryName, primaryName));
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("If you want to keep living in here, you'd best not lose your equipment...");
            }
        }
    }
}
