package rootPackage.Level.Features.Equipment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Equipment.AccessoryEffects.AccessoryEffect;
import rootPackage.Level.Features.Equipment.AccessoryEffects.AccessoryEffectAlias;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A {@link rootPackage.Level.Features.Feature Feature} representing an accessory.
 * <p></p>
 * Accessories can have any effect, such as a bonus to HP or a higher attack stat when fighting beasts.
 *
 * @version 1.0
 * @author William Owens
 */
public class AccessoryFeature extends EquipmentFeature {

    private String statNotification;
    private ArrayList<AccessoryEffect> effects = new ArrayList<>();
    private String effectDescriptionForStatus;

    public AccessoryFeature(String primaryName, String[] allNames, String jsonPath) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.EQUIPPABLE);
        flags.add(FeatureFlag.ACCESSORY);
        JSONParser parser = new JSONParser();
        try {
            FileReader fr = new FileReader("json"+System.getProperty("file.separator")+"equipment"+System.getProperty("file.separator")+jsonPath);
            JSONObject jsonFile = (JSONObject) parser.parse(fr);
            description = (String) jsonFile.get("description");
            statNotification = (String) jsonFile.get("statNotification");
            actionsJsonField = (JSONArray) jsonFile.get("actionsGranted");
            descriptionsJsonField = (JSONArray) jsonFile.get("actionDescriptions");
            effectDescriptionForStatus = (String) jsonFile.get("effectDescriptionForStatus");
            JSONArray effectsJsonField = (JSONArray) jsonFile.get("effects");
            for (Object effectName : effectsJsonField) {
                AccessoryEffect effect = AccessoryEffectAlias.getEffect((String) effectName);
                effects.add(effect);
            }
        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<AccessoryEffect> getEffects() {
        return effects;
    }

    public String getEffectDescriptionForStatus() {
        return effectDescriptionForStatus;
    }

    @Override
    public void react(PlayerAction playerAction) {
        Feature playerAsFeature = Main.player.getPlayerAsFeature();
        ConsoleWindow consoleWindow = Main.mainWindow.getConsoleWindow();
        switch (playerAction) {
            case USE, EQUIP, PICKUP -> {
                // unlike armor, weapons or shields, the player is not limited on the number of accessories they can wear
                this.reparentSelf(playerAsFeature);
                consoleWindow.addEntryToHistory("You equip the %s. %s".formatted(this.getPrimaryName(), statNotification));
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

