package rootPackage.Level.Features.Equipment;

import org.json.simple.*;

/**
 * Class responsible for mapping a name to a specific equipment feature.
 *
 * @author William Owens
 * @version 1.04
 */
public class EquipmentAlias {

    public static EquipmentFeature getEquipment(String name) {
        switch (name) {
            case "steelSword" -> {
                return new WeaponFeature("Steel Sword", new String[]{"sword", "blade"}, "steelSword.json");
            }
            case "steelArmor" -> {
                return new ArmorFeature("Steel Armor", new String[]{"armor"}, "steelArmor.json");
            }
            case "steelShield" -> {
                return new ShieldFeature("Steel Shield", new String[]{"shield"}, "steelShield.json");
            }
            case "speedTalisman" -> {
                return new AccessoryFeature("Talisman of Hermes", new String[]{"talisman"}, "speedTalisman.json");
            }
            case "minotaurAxe" -> {
                return new WeaponFeature("Axe of the Minotaur", new String[]{"axe", "minotaur axe", "blood axe", "bloody axe"}, "minotaurAxe.json");
            }
            case "healthTalisman" -> {
                return new AccessoryFeature("Health Talisman", new String[]{"talisman"}, "healthTalisman.json");
            }
            case "attackTalisman" -> {
                return new AccessoryFeature("Attack Talisman", new String[]{"talisman"}, "attackTalisman.json");
            }
            case "defenseTalisman" -> {
                return new AccessoryFeature("Defense Talisman", new String[]{"talisman"}, "defenseTalisman.json");
            }
        }
        return null;
    }

}
