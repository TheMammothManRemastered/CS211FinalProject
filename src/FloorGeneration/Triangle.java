package FloorGeneration;

import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * A clone of java.awt.Polygon, but with some features changed (mainly the points are stored with double precision).
 * On a technical level, this class behaves similarly to a Polygon, but it can't have more than 3 coordinates.
 * It also cannot do the drawing and graphics stuff that Polygons can.
 */
class Triangle {

    private int nPoints;
    public double[] xPoints;
    public double[] yPoints;

    private static final int LENGTH = 3;

    public Triangle() {
        xPoints = new double[LENGTH];
        yPoints = new double[LENGTH];
    }

    public Triangle(double[] xPoints, double[] yPoints) {
        this(xPoints,yPoints,3);
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
     * Add a specified coordinate to the triangle.
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
     * Add a specified coordinate to the triangle.
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
    //TODO: this uses equivalence with doubles. determine a tolerance and implement that.
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
        Point2D.Double pointA = new Point2D.Double(xPoints[0],yPoints[0]);
        Point2D.Double pointB = new Point2D.Double(xPoints[1],yPoints[1]);
        Point2D.Double pointC = new Point2D.Double(xPoints[2],yPoints[2]);
        // midpoints between a and b, and b and c
        Point2D.Double pointAB = new Point2D.Double( (pointA.x + pointB.x)/2.0 ,  (pointA.y + pointB.y)/2.0 );
        Point2D.Double pointBC = new Point2D.Double( (pointB.x + pointC.x)/2.0 ,  (pointB.y + pointC.y)/2.0 );

        // distances between x and y components of set a and set b
        double deltaYSetA = pointB.y - pointA.y;
        double deltaXSetA = pointB.x - pointA.x;
        double deltaYSetB = pointC.y - pointB.y;
        double deltaXSetB = pointC.x - pointB.y;

        // slopes for sets a and b
        double setASlope = deltaYSetA/deltaXSetA;
        double setBSlope = deltaYSetB/deltaXSetB;

        if (deltaYSetA == 0) { // this means the slope of set a is 0
            centerPoint.x = pointAB.x; // this can be assumed true if points a and b are on the same y coord
            if (deltaXSetB == 0) { // if the slope of set b is infinity (meaning vertical), this can be assumed true
                centerPoint.y = pointBC.y;
            }
            else { // otherwise, calculate like so
                centerPoint.y = pointBC.y + (pointBC.x- centerPoint.x)/setBSlope;
            }
        }
        else if (deltaYSetB == 0) { // this means the slope of set b is 0
            centerPoint.x = pointBC.x; // can be assumed true
            if (deltaXSetA == 0) { // slope of set a is infinity (meaning vertical)
                centerPoint.y = pointAB.y; // can be assumed true
            }
            else { // same calculation as above
                centerPoint.y = pointAB.y + (pointAB.x - centerPoint.x)/setASlope;
            }
        }
        else if (deltaXSetA == 0) { // slope of set a is infinity
            centerPoint.y = pointAB.y; // can be assumed
            centerPoint.x = setBSlope*(pointBC.y- centerPoint.y) + pointBC.x;
        }
        else if (deltaXSetB == 0) { // slope of set b is infinity
            centerPoint.y = pointBC.y;
            centerPoint.x = setASlope*(pointAB.y - centerPoint.y) + pointAB.x;
        }
        else { // general case
            centerPoint.x = (setASlope*setBSlope*(pointAB.y-pointBC.y) - setASlope*pointBC.x + setBSlope*pointAB.x)/(setBSlope-setASlope);
            centerPoint.y = pointAB.y - (centerPoint.x - pointAB.x)/setASlope;
        }

        // now, we have the center of the circle
        // to get radius, plug new values into general form of a circle

        radius = Math.sqrt(Math.pow(pointA.x - centerPoint.x, 2) + Math.pow(pointA.y - centerPoint.y, 2));

        return new Circle(centerPoint.x, centerPoint.y, radius);
    }

    /**
     * Gets a point in the triangle by index.
     */
    public Point2D.Double getPoint(int index) {
        if (index > LENGTH) {
            throw new IndexOutOfBoundsException("Index "+index+" is out of bounds.");
        }

        return new Point2D.Double(xPoints[index],yPoints[index]);
    }

    @Override
    public String toString() {
        return "Triangle of points [%f, %f], [%f, %f], [%f, %f]".formatted(
                xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2]
        );
    }
}
