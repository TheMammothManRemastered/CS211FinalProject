package rootPackage.Level.FloorGeneration.Templates;

import org.json.simple.*;
import rootPackage.Level.FloorGeneration.Templates.Templates.GenericTreasureRoom;
import rootPackage.Level.FloorGeneration.Templates.Templates.IntraFloorShop;
import rootPackage.Level.FloorGeneration.Templates.Templates.MinotaurBossRoom;
import rootPackage.Level.FloorGeneration.Templates.Templates.MinotaurKeyRoom;

/**
 * Class responsible for mapping a name to a specific room template.
 *
 * @author William Owens
 * @version 1.02
 */
public class RoomAlias {

    public static RoomTemplate getTemplate(String name) {
        switch (name) {
            case "minotaurBoss" -> {
                return new MinotaurBossRoom();
            }
            case "minotaurKey" -> {
                return new MinotaurKeyRoom();
            }
            case "treasureRoomGeneric" -> {
                return new GenericTreasureRoom();
            }
            case "shop" -> {
                return new IntraFloorShop();
            }
        }
        return null;
    }

}
