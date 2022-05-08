package rootPackage.Level.Features.Equipment;

import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A {@link rootPackage.Level.Features.Feature Feature} representing a suit of equippable armor.
 * <p></p>
 * Armor is responsible for setting the player's HP stat.
 *
 * @author William Owens
 * @version 1.5
 */
public class ArmorFeature extends EquipmentFeature {

    public ArmorFeature(String primaryName, String[] allNames, String jsonPath) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.ARMOR);
        flags.add(FeatureFlag.EQUIPPABLE);
        JSONParser parser = new JSONParser();
        try {
            FileReader fr = new FileReader("json" + System.getProperty("file.separator") + "equipment" + System.getProperty("file.separator") + jsonPath);
            JSONObject jsonFile = (JSONObject) parser.parse(fr);
            value = Math.toIntExact((Long) jsonFile.get("value"));
            description = (String) jsonFile.get("description");
            actionsJsonField = (JSONArray) jsonFile.get("actionsGranted");
            descriptionsJsonField = (JSONArray) jsonFile.get("actionDescriptions");

        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void react(PlayerAction playerAction) {
        Feature roomAsFeature = Main.player.getCurrentRoom().getRoomAsFeature();
        Feature playerAsFeature = Main.player.getPlayerAsFeature();
        ConsoleWindow consoleWindow = Main.mainWindow.getConsoleWindow();
        switch (playerAction) {
            case USE, EQUIP, PICKUP -> {
                ArrayList<Feature> playerArmorFeatures = playerAsFeature.getChildren(FeatureFlag.ARMOR);
                if (playerArmorFeatures.size() != 0) {
                    // the player has no armor equipped, proceed normally, otherwise it has to be doffed
                    for (Feature armor : playerArmorFeatures) {
                        consoleWindow.addEntryToHistory("You doff your %s and place it carefully on the ground.".formatted(armor.getPrimaryName()));
                        armor.reparentSelf(roomAsFeature);
                    }
                }
                this.reparentSelf(playerAsFeature);
                consoleWindow.addEntryToHistory("You don the %s. Your HP is now %d".formatted(this.getPrimaryName(), value)); //TODO: load armor stat from json
            }
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory(description);
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The armor has no keyhole of any kind! What are you playing at?");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("For some odd reason, you think that attacking a piece of armor, something designed specifically to withstand attacks, isn't a very good idea...");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("If you want to keep living in here, you'd best not lose your equipment...");
            }
        }
    }
}
