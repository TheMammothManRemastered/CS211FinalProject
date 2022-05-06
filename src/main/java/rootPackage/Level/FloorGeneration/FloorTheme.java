package rootPackage.Level.FloorGeneration;

import rootPackage.Level.Features.Feature;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

public class FloorTheme {

    private int minimumSize, maximumSize, deadEndsNeeded, healthPoints;
    private RoomTemplate exitRoomFeatures;
    private RoomTemplate keyRoomFeatures;
    private Feature[][] themedRoomFeatures;
    private Feature[] themedFeatures;
    private Feature[] enemies;
    private String keyName;
    private String bossDoorDescription;

    public FloorTheme(int minimumSize, int maximumSize, int deadEndsNeeded, int healthPoints, RoomTemplate exitRoomFeatures, RoomTemplate keyRoomFeatures, Feature[][] themedRoomFeatures, Feature[] themedFeatures, Feature[] enemies, String keyName, String bossDoorDescription) {
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
        this.deadEndsNeeded = deadEndsNeeded;
        this.healthPoints = healthPoints;
        this.exitRoomFeatures = exitRoomFeatures;
        this.keyRoomFeatures = keyRoomFeatures;
        this.themedRoomFeatures = themedRoomFeatures;
        this.themedFeatures = themedFeatures;
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
