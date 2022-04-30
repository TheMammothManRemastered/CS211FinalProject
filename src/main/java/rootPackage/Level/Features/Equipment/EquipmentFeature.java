package rootPackage.Level.Features.Equipment;

import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Modifers.ModifierFeature;
import rootPackage.Level.Features.Modifers.StatName;

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
    protected StatName affectedStat;
    protected ModifierFeature[] battleEffects;

    public EquipmentFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
    }

    public EquipmentFeature(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        super(primaryName, allNames, parent, children, flags);
    }

    public double getValue() {
        return value;
    }

    public ModifierFeature[] getBattleEffects() {
        return battleEffects;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setBattleEffects(ModifierFeature[] battleEffects) {
        this.battleEffects = battleEffects;
    }

    public StatName getAffectedStat() {
        return affectedStat;
    }

    public void setAffectedStat(StatName affectedStat) {
        this.affectedStat = affectedStat;
    }
}
