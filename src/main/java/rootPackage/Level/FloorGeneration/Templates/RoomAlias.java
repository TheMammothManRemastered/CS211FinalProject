package rootPackage.Level.FloorGeneration.Templates;

import rootPackage.Level.FloorGeneration.Templates.Templates.GenericTreasureRoom;
import rootPackage.Level.FloorGeneration.Templates.Templates.IntraFloorShop;
import rootPackage.Level.FloorGeneration.Templates.Templates.MinotaurBossRoom;
import rootPackage.Level.FloorGeneration.Templates.Templates.MinotaurKeyRoom;

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
