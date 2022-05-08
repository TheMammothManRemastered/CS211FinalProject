package rootPackage.Level.Features.Equipment;

import org.json.simple.*;
import org.json.simple.JSONArray;
import rootPackage.Battle.Actions.Action;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;

import java.util.ArrayList;

/**
 * Parent abstract class of all equipment-type {@link rootPackage.Level.Features.Feature Features}.
 *
 * @author William Owens
 * @version 1.0
 */
public abstract class EquipmentFeature extends Feature {

    //TODO: the actionsGranted should be refreshed on battle start and every action execution

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
