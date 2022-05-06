package rootPackage.Level.Features.Equipment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.BattleSupervisor;
import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Battle.Intents.Intent;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;
import rootPackage.Player;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parent abstract class of all equipment-type {@link rootPackage.Level.Features.Feature Features}.
 *
 * @author William Owens
 * @version 1.0
 */
public abstract class EquipmentFeature extends Feature {

    //TODO: this should also have a list of Actions it grants to the player

    protected double value;
    protected String jsonPath;
    protected String description;
    protected ArrayList<Action> actions;
    protected JSONArray actionsJsonField;
    protected JSONArray descriptionsJsonField;

    public EquipmentFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        actions = new ArrayList<>();
    }

    public EquipmentFeature(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        super(primaryName, allNames, parent, children, flags);
        actions = new ArrayList<>();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public JSONArray getActionsJsonField() {
        return actionsJsonField;
    }

    public void setActionsJsonField(JSONArray actionsJsonField) {
        this.actionsJsonField = actionsJsonField;
    }

    public JSONArray getDescriptionsJsonField() {
        return descriptionsJsonField;
    }

    public void setDescriptionsJsonField(JSONArray descriptionsJsonField) {
        this.descriptionsJsonField = descriptionsJsonField;
    }

}
