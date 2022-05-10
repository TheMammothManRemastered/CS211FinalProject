package rootPackage;

/**
 * A class containing methods to compare two floats for equivalence.
 *
 * @author William Owens (and every other person who's ever had to compare floats)
 * @version 1.1
 */
public class FloatEquivalence {

    //TODO: apparently a static float tolerance is bad practice? fix this if problems arise.
    private final static double FLOAT_TOLERANCE = 0.00000001;

    public static boolean equals(double a, double b, double tolerance) {
        return (Math.abs(a - b) < tolerance);
    }

    public static boolean equals(double a, double b) {
        return equals(a, b, FLOAT_TOLERANCE);
    }
}
