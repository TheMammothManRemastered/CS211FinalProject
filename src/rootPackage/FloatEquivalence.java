package rootPackage;

/**
 * A class containing methods to compare two floats for equivalence.
 *
 * @version 1.0
 * @author William Owens (and every other person who's ever had to compare floats)
 */
public class FloatEquivalence {

    //TODO: apparently a static float tolerance is bad practice? fix this if problems arise.
    private static double FLOAT_TOLERANCE = 0.00000001;

    public static boolean floatEquivalence(double a, double b, double tolerance) {
        return (Math.abs(a - b) < tolerance);
    }

    public static boolean floatEquivalence(double a, double b) {
        return floatEquivalence(a, b, FLOAT_TOLERANCE);
    }
}
