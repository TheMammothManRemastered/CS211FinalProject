package rootPackage;

public class FloatEquivalence {

    private static double FLOAT_TOLERANCE = 0.00000001;

    public static boolean floatEquivalence(double a, double b, double tolerance) {
        return (Math.abs(a - b) < tolerance);
    }

    public static boolean floatEquivalence(double a, double b) {
        return floatEquivalence(a, b, FLOAT_TOLERANCE);
    }
}
