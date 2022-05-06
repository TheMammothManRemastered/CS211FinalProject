package rootPackage.Level.FloorGeneration;

import java.util.Random;

/**
 * A standardized reference to all rng for floor generation.
 */
public class FloorGenerationRNG {
    //NOTE: I'm not sure if having rng be static is good practice or if it will cause problems
    //TODO: unseed this
    public static Random rng = new Random(44);

    public static boolean roll() {
        return rng.nextBoolean();
    }

}
