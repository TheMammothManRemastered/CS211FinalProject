package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

public class DefenseTalismanEffect extends AccessoryEffect{

    public DefenseTalismanEffect() {
        value = 0.05;
        mode = Modes.ADD;
        affectedStat = "blockStat";
    }

}
