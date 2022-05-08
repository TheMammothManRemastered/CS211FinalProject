package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

public class SacredTalismanEffect extends AccessoryEffect{

    public SacredTalismanEffect() {
        value = 3;
        mode = Modes.SET;
        affectedStat = "priorityStat";
    }
}
