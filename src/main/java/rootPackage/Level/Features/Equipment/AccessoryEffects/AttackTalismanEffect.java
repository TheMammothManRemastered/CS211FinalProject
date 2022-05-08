package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

public class AttackTalismanEffect extends AccessoryEffect{

    public AttackTalismanEffect() {
        value = 20;
        mode = Modes.ADD;
        affectedStat = "attackStat";
    }

}
