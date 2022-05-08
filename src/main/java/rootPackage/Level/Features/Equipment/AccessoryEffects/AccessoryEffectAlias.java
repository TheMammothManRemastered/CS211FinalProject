package rootPackage.Level.Features.Equipment.AccessoryEffects;

import org.json.simple.*;

/**
 * Class responsible for mapping a name to a specific accessory effect.
 *
 * @author William Owens
 * @version 1.01
 */
public class AccessoryEffectAlias {

    public static AccessoryEffect getEffect(String effectName) {
        switch (effectName) {
            default -> {
                return new SacredTalismanEffect();
            }
            case "HealthTalismanEffect" -> {
                return new HealthTalismanEffect();
            }
            case "AttackTalismanEffect" -> {
                return new AttackTalismanEffect();
            }
            case "DefenseTalismanEffect" -> {
                return new DefenseTalismanEffect();
            }
        }
    }

}
