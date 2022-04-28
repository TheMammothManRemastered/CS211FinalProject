package rootPackage.Level.FloorGeneration;

import rootPackage.Level.Features.Feature;

public class FloorTheme {

    private int minimumSize, maximumSize, deadEndsNeeded, healthPoints;
    private Feature[] exitRoomFeatures;
    private Feature[] keyRoomFeatures;
    private Feature[][] themedRoomFeatures;
    private Feature[] themedFeatures;
    private Feature[] enemies;

    public FloorTheme(int minimumSize, int maximumSize, int deadEndsNeeded, int healthPoints, Feature[] exitRoomFeatures, Feature[] keyRoomFeatures, Feature[][] themedRoomFeatures, Feature[] themedFeatures, Feature[] enemies) {
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
        this.deadEndsNeeded = deadEndsNeeded;
        this.healthPoints = healthPoints;
        this.exitRoomFeatures = exitRoomFeatures;
        this.keyRoomFeatures = keyRoomFeatures;
        this.themedRoomFeatures = themedRoomFeatures;
        this.themedFeatures = themedFeatures;
        this.enemies = enemies;
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
}
