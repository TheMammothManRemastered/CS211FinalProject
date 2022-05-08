package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

/**
 * A class representing any one of the stat changes granted by an accessory.
 *
 * @version 1.0
 * @author William Owens
 */
public class AccessoryEffect {

    protected double value;
    protected Modes mode;
    protected String affectedStat;

    public double getValue() {
        return value;
    }

    public Modes getMode() {
        return mode;
    }

    public String getAffectedStat() {
        return affectedStat;
    }
}
