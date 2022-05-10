package rootPackage.Level.FloorGeneration;

import rootPackage.Level.Features.Feature;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

/**
 * Data storage class, represents a theme for a floor, all the parameters a floor must adhere to.
 */
public class FloorTheme {

    private final int minimumSize, maximumSize, deadEndsNeeded, healthPoints;
    private final RoomTemplate exitRoomFeatures, keyRoomFeatures;
    private final Feature[] enemies;
    private final String keyName, bossDoorDescription;

    public FloorTheme(int minimumSize, int maximumSize, int deadEndsNeeded, int healthPoints, RoomTemplate exitRoomFeatures, RoomTemplate keyRoomFeatures, Feature[] enemies, String keyName, String bossDoorDescription) {
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
        this.deadEndsNeeded = deadEndsNeeded;
        this.healthPoints = healthPoints;
        this.exitRoomFeatures = exitRoomFeatures;
        this.keyRoomFeatures = keyRoomFeatures;
        this.enemies = enemies;
        this.keyName = keyName;
        this.bossDoorDescription = bossDoorDescription;
    }

    public int getMinimumSize() {
        return minimumSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public int getDeadEndsNeeded() {
        return deadEndsNeeded;
    }

    public RoomTemplate getExitRoomFeatures() {
        return exitRoomFeatures;
    }

    public RoomTemplate getKeyRoomFeatures() {
        return keyRoomFeatures;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getBossDoorDescription() {
        return bossDoorDescription;
    }

    public Feature[] getEnemies() {
        return enemies;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}
