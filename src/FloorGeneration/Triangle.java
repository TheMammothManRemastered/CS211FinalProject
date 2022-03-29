package FloorGeneration;

import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * A clone of java.awt.Polygon, but with some features changed (mainly the points are stored with double precision).
 * On a technical level, this class behaves similarly to a Polygon, but it can't have more than 3 coordinates.
 * It also cannot be drawn in the way that Polygons can.
 */
class Triangle {

    //TODO: apparently a static float tolerance isn't good practice. i'll have to look into this more if problems arise
    private final static double FLOAT_TOLERANCE = 0.00000001;
    private static final int LENGTH = 3;

    private int nPoints;
    public double[] xPoints;
    public double[] yPoints;

    /**
     * Constructor. Creates a triangle with no points.
     */
    public Triangle() {
        xPoints = new double[LENGTH];
        yPoints = new double[LENGTH];
    }

    /**
     * Constructor. Creates a triangle with the given x and y coordinates.
     *
     * @param xPoints An array containing the x components of the triangle's points.
     * @param yPoints An array containing the y components of the triangle's points.
     */
    public Triangle(double[] xPoints, double[] yPoints) {
        this(xPoints, yPoints, 3);
    }

    private Triangle(double[] xPoints, double[] yPoints, int nPoints) {

        if (nPoints > xPoints.length || nPoints > yPoints.length) {
            throw new ArrayIndexOutOfBoundsException("there must be at least nPoints elements in xPoints and yPoints");
        }

        if (nPoints < 0) {
            throw new NegativeArraySizeException("nPoints cannot be less than 0");
        }

        this.xPoints = Arrays.copyOf(xPoints, nPoints);
        this.yPoints = Arrays.copyOf(yPoints, nPoints);
    }

    /**
     * Removes all points from the triangle.
     */
    public void reset() {
        nPoints = 0;
    }

    /**
     * Adds a specified point to the triangle.
     *
     * @param x The x coordinate to add.
     * @param y The y coordinate to add.
     */
    public void addPoint(double x, double y) {
        int newLength = nPoints + 1;
        if (newLength < LENGTH) {
            newLength = LENGTH;
        } else if (newLength > LENGTH) {
            throw new IndexOutOfBoundsException("A triangle can only hold 3 points.");
        }


        xPoints = Arrays.copyOf(xPoints, newLength);
        yPoints = Arrays.copyOf(yPoints, newLength);

        xPoints[nPoints] = x;
        yPoints[nPoints] = y;
        nPoints++;
    }

    /**
     * Adds a specified point to the triangle.
     *
     * @param point The point to add.
     */
    public void addPoint(Point2D.Double point) {
        double x = point.getX();
        double y = point.getY();

        int newLength = nPoints + 1;
        if (newLength < LENGTH) {
            newLength = LENGTH;
        } else if (newLength > LENGTH) {
            throw new IndexOutOfBoundsException("A triangle can only hold 3 points.");
        }

        xPoints = Arrays.copyOf(xPoints, newLength);
        yPoints = Arrays.copyOf(yPoints, newLength);

        xPoints[nPoints] = x;
        yPoints[nPoints] = y;
        nPoints++;
    }

    /**
     * Method not taken wholesale from Polygon.
     * Generates a circumcircle for the points of the triangle.
     */
    public Circle getCircumcircle() {
        if (nPoints < 3) {
            throw new ArithmeticException("This triangle must contain 3 points to calculate a circumcircle");
        }

        // http://paulbourke.net/geometry/circlesphere/
        // https://stackoverflow.com/questions/4103405/what-is-the-algorithm-for-finding-the-center-of-a-circle-from-three-points
        // going with the approach of doing it generally, unless there are vertical and horizontal slopes
        // if that is the case, more specific math needs to be done

        // define center coordinate and radius here
        Point2D.Double centerPoint = new Point2D.Double();
        double radius = 0.0;

        // points a, b, and c
        // point a and point b make up set a
        // point b and point c make up set b
        Point2D.Double pointA = new Point2D.Double(xPoints[0], yPoints[0]);
        Point2D.Double pointB = new Point2D.Double(xPoints[1], yPoints[1]);
        Point2D.Double pointC = new Point2D.Double(xPoints[2], yPoints[2]);
        // midpoints between a and b, and b and c
        Point2D.Double pointAB = new Point2D.Double((pointA.x + pointB.x) / 2.0, (pointA.y + pointB.y) / 2.0);
        Point2D.Double pointBC = new Point2D.Double((pointB.x + pointC.x) / 2.0, (pointB.y + pointC.y) / 2.0);

        // distances between x and y components of set a and set b
        double deltaYSetA = pointB.y - pointA.y;
        double deltaXSetA = pointB.x - pointA.x;
        double deltaYSetB = pointC.y - pointB.y;
        double deltaXSetB = pointC.x - pointB.x;

        // slopes for sets a and b
        double setASlope = deltaYSetA / deltaXSetA;
        double setBSlope = deltaYSetB / deltaXSetB;

        if (floatEquivalence(deltaYSetA, 0)) { // this means the slope of set a is 0
            centerPoint.x = pointAB.x; // this can be assumed true if points a and b are on the same y coord
            if (floatEquivalence(deltaXSetB, 0)) { // if the slope of set b is infinity (meaning vertical), this can be assumed true
                centerPoint.y = pointBC.y;
            } else { // otherwise, calculate like so
                centerPoint.y = pointBC.y + (pointBC.x - centerPoint.x) / setBSlope;
            }
        } else if (floatEquivalence(deltaYSetB, 0)) { // this means the slope of set b is 0
            centerPoint.x = pointBC.x; // can be assumed true
            if (floatEquivalence(deltaXSetA, 0)) { // slope of set a is infinity (meaning vertical)
                centerPoint.y = pointAB.y; // can be assumed true
            } else { // same calculation as above
                centerPoint.y = pointAB.y + (pointAB.x - centerPoint.x) / setASlope;
            }
        } else if (floatEquivalence(deltaXSetA, 0)) { // slope of set a is infinity
            centerPoint.y = pointAB.y; // can be assumed
            centerPoint.x = setBSlope * (pointBC.y - centerPoint.y) + pointBC.x;
        } else if (floatEquivalence(deltaXSetB, 0)) { // slope of set b is infinity
            centerPoint.y = pointBC.y;
            centerPoint.x = setASlope * (pointAB.y - centerPoint.y) + pointAB.x;
        } else { // general case
            centerPoint.x = (setASlope * setBSlope * (pointA.y - pointC.y) + setBSlope * (pointA.x + pointB.x) - setASlope * (pointB.x + pointC.x)) / (2.0 * (setBSlope - setASlope));
            centerPoint.y = ((-1.0 / setASlope) * (centerPoint.x - (((pointA.x + pointB.x) / 2.0)))) + ((pointA.y + pointB.y) / 2.0);
        }

        // now, we have the center of the circle
        // to get radius, plug new values into general form of a circle

        radius = Math.sqrt(Math.pow(pointA.x - centerPoint.x, 2) + Math.pow(pointA.y - centerPoint.y, 2));

        return new Circle(centerPoint.x, centerPoint.y, radius);
    }

    /**
     * Translates the triangle by the given offsets.
     * @param xOffset The x component of the translation.
     * @param yOffset The y component of the translation.
     */
    public void translate(double xOffset, double yOffset) {
        for (int d = 0; d < nPoints; d++) {
            xPoints[d] += xOffset;
            yPoints[d] += yOffset;
        }
    }

    public static boolean floatEquivalence(double a, double b, double tolerance) {
        return (Math.abs(a - b) < tolerance);
    }

    public static boolean floatEquivalence(double a, double b) {
        return floatEquivalence(a, b, FLOAT_TOLERANCE);
    }

    /**
     * Gets a point in the triangle by its index.
     */
    public Point2D.Double getPoint(int index) {
        if (index > LENGTH) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }

        return new Point2D.Double(xPoints[index], yPoints[index]);
    }

    @Override
    public String toString() {
        return "Triangle of points [%f, %f], [%f, %f], [%f, %f]".formatted(
                xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2]
        );
    }

    @Override
    public boolean equals(Object obj) {
        // we'll assume the only thing being compared here are other triangles
        Triangle tri = (Triangle) obj;
        double[] xTri1 = this.xPoints.clone();
        double[] yTri1 = this.yPoints.clone();
        double[] xTri2 = tri.xPoints.clone();
        double[] yTri2 = tri.yPoints.clone();
        Arrays.sort(xTri1);
        Arrays.sort(yTri1);
        Arrays.sort(xTri2);
        Arrays.sort(yTri2);

        return (Arrays.equals(xTri1, xTri2) && Arrays.equals(yTri1, yTri2));

    }

}
