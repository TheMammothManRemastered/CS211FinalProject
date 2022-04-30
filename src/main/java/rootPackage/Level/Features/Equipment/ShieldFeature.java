package rootPackage.Level.Features.Equipment;

import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

/**
 * A {@link rootPackage.Level.Features.Feature Feature} representing an equippable shield.
 * <p></p>
 * Shields are responsible for setting the player's defence stat.
 *
 * @version 1.0
 * @author William Owens
 */
public class ShieldFeature extends Feature {

    public ShieldFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.SHIELD);
        flags.add(FeatureFlag.EQUIPPABLE);
    }

    @Override
    public void react(PlayerAction playerAction) {
        Feature roomAsFeature = Main.player.getCurrentRoom().getRoomAsFeature();
        Feature playerAsFeature = Main.player.getPlayerAsFeature();
        ConsoleWindow consoleWindow = Main.mainWindow.getConsoleWindow();
        switch (playerAction) {
            case USE, EQUIP, PICKUP -> {
                ArrayList<Feature> playerShieldFeatures = playerAsFeature.getChildren(FeatureFlag.SHIELD);
                if (playerShieldFeatures.size() != 0) {
                    // the player has no weapon equipped, proceed normally, otherwise it has to be removed
                    for (Feature shield : playerShieldFeatures) {
                        consoleWindow.addEntryToHistory("You remove your %s and place it on the ground.".formatted(shield.getPrimaryName()));
                        shield.reparentSelf(roomAsFeature);
                    }
                }
                this.reparentSelf(playerAsFeature);
                consoleWindow.addEntryToHistory("You strap the %s securely to your arm. Your defense is now %d".formatted(this.getPrimaryName(), 666666)); //TODO: load damage stat from json
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The shield has no keyhole of any kind! What are you playing at?");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("For some odd reason, you think that attacking a shield, something designed specifically to withstand attacks, isn't a very good idea...");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("If you want to keep living in here, you'd best not lose your equipment...");
            }
        }
    }
}
