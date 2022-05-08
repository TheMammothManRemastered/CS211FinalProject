package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

public class HealthTalismanEffect extends AccessoryEffect{

    public HealthTalismanEffect() {
        value = 70;
        mode = Modes.ADD;
        affectedStat = "hpStat";
    }
}

