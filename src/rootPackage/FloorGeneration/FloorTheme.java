package rootPackage.FloorGeneration;

import rootPackage.FloorGeneration.Features.Feature;

public class FloorTheme {

    private int minimumSize, deadEndsNeeded, healthPoints;
    private Feature[] exitRoomFeatures;
    private Feature[] keyRoomFeatures;
    private Feature[][] themedRoomFeatures;
    private Feature[] themedFeatures;
    private Feature[] enemies;

    public FloorTheme(int minimumSize, int deadEndsNeeded, int healthPoints, Feature[] exitRoomFeatures, Feature[] keyRoomFeatures, Feature[][] themedRoomFeatures, Feature[] themedFeatures, Feature[] enemies) {
        this.minimumSize = minimumSize;
        this.deadEndsNeeded = deadEndsNeeded;
        this.healthPoints = healthPoints;
        this.exitRoomFeatures = exitRoomFeatures;
        this.keyRoomFeatures = keyRoomFeatures;
        this.themedRoomFeatures = themedRoomFeatures;
        this.themedFeatures = themedFeatures;
        this.enemies = enemies;
    }
}
