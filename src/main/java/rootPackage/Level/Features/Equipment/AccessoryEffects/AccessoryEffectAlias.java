package rootPackage.Level.Features.Equipment.AccessoryEffects;

public class AccessoryEffectAlias {

    public static AccessoryEffect getEffect(String effectName) {
        switch (effectName) {
            default -> {
                return  new SacredTalismanEffect();
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
