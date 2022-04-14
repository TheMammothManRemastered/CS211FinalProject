package rootPackage.FloorGeneration.Features;

/**
 * Interface used by features that can be unlocked (ie. doors, chests)
 */
public interface Unlockable {

    boolean locked = false;
    Feature requiredKey = null;

    public boolean tryUnlock(Feature keyBeingUsed);

    public boolean isLocked();

}
