package rootPackage.Level.FloorGeneration;

import java.util.Random;

/**
 * A standardized reference to all rng for floor generation.
 */
public class FloorGenerationRNG {
    //NOTE: I'm not sure if having rng be static is good practice or if it will cause problems, look into that
    //TODO: unseed this
    public static Random rng = new Random();
}
