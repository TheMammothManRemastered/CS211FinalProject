package rootPackage.FloorGeneration.Layout;

import rootPackage.FloatEquivalence;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The triangle class stores a triangle, the points that comprise it, and its adjacent triangles
 */
class Triangle {

    //TODO: rework triangle (or, easier, make another triangle class)
    // triangles need to keep track of their adjacencies
    // every time the master array of triangles is updated at all, make triangles re-evaluate their adjacencies
    // this absolutely can be optimized better, only check concerned triangles?
    // make the triangle class not be just polygon 2: bad edition
    // the arrays containing points is fine, but make obtaining all points more sensical
    // constructor should take all three points at once
    // add a getNormals() function to get a triangle's normals
    // keep track of adjacencies, and make sure there's a getAdjacencies function

    private final int NUM_POINTS = 3;

    private double[] xPoints, yPoints;
    private Triangle[] adjacentTriangles;

    public Triangle(double[] xPoints, double[] yPoints) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.adjacentTriangles = new Triangle[3];
    }

    public Triangle(double[] xPoints, double[] yPoints, Triangle[] adjacentTriangles) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.adjacentTriangles = Arrays.copyOf(adjacentTriangles,adjacentTriangles.length);
    }

    private double getArea() {
        return Math.abs((xPoints[0] * (yPoints[1] - yPoints[2]) +
                xPoints[1] * (yPoints[2] - yPoints[1]) +
                xPoints[3] * (yPoints[0] - yPoints[1])) / 2.0);
    }

    public boolean containsPoint(MyPoint2D point) {
        Triangle[] triangles = new Triangle[3];
        for (int i = 0; i < 3; i++) {
            triangles[i] = new Triangle(
                    new double[]{point.x, this.xPoints[i],
                            (i + 1 == 3) ? this.xPoints[0] : this.xPoints[i + 1]},
                    new double[]{point.y, this.yPoints[i],
                            (i + 1 == 3) ? this.yPoints[0] : this.yPoints[i + 1]}
            );
        }
        double subArea = 0;
        for (int i = 0; i < 3; i++) {
            subArea += triangles[i].getArea();
        }
        return FloatEquivalence.floatEquivalence(this.getArea(), subArea);
    }

    private void sortCCW() {

        int indexOfMaxX = 0;
        double xMax = Double.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            if (xPoints[i] > xMax) {
                xMax = xPoints[i];
                indexOfMaxX = i;
            }
        }

    }

    /**
     * @return
     * @see <a href="http://paulbourke.net/geometry/circlesphere/"></a>
     */
    public Circle getCircumcircle() {
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


        if (FloatEquivalence.floatEquivalence(deltaYSetA, 0)) { // this means the slope of set a is 0
            centerPoint.x = pointAB.x; // this can be assumed true if points a and b are on the same y coord
            if (FloatEquivalence.floatEquivalence(deltaXSetB, 0)) { // if the slope of set b is infinity (meaning vertical), this can be assumed true
                centerPoint.y = pointBC.y;
            } else { // otherwise, calculate like so
                centerPoint.y = pointBC.y + (pointBC.x - centerPoint.x) / setBSlope;
            }
        } else if (FloatEquivalence.floatEquivalence(deltaYSetB, 0)) { // this means the slope of set b is 0
            centerPoint.x = pointBC.x; // can be assumed true
            if (FloatEquivalence.floatEquivalence(deltaXSetA, 0)) { // slope of set a is infinity (meaning vertical)
                centerPoint.y = pointAB.y; // can be assumed true
            } else { // same calculation as above
                centerPoint.y = pointAB.y + (pointAB.x - centerPoint.x) / setASlope;
            }
        } else if (FloatEquivalence.floatEquivalence(deltaXSetA, 0)) { // slope of set a is infinity
            centerPoint.y = pointAB.y; // can be assumed
            centerPoint.x = setBSlope * (pointBC.y - centerPoint.y) + pointBC.x;
        } else if (FloatEquivalence.floatEquivalence(deltaXSetB, 0)) { // slope of set b is infinity
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
     * Returns a copy of this triangle's adjacent triangles.
     */
    public ArrayList<Triangle> getAdjacentTriangles() {
        return new ArrayList<>(adjacentTriangles);
    }

    /**
     * Returns the actual object reference to this triangle's adjacent triangles.
     */
    public ArrayList<Triangle> getAdjacentTrianglesObject() {
        return adjacentTriangles;
    }

    public void setAdjacentTriangles(ArrayList<Triangle> adjacentTriangles) {
        this.adjacentTriangles = adjacentTriangles;
    }

    public void evauateAdjacenctTriangles(ArrayList<Triangle> allTriangles) {
        for (Triangle tri : allTriangles) {
            if (this.equals(tri)) {
                continue;
            }
            boolean added = false;
            for (Point2D.Double point : this.getPoints()) {
                for (Point2D.Double triPoint : tri.getPoints()) {
                    if (point.equals(triPoint)) {
                        this.adjacentTriangles.add(tri);
                        added = true;
                        break;
                    }
                }
                if (added) {
                    break;
                }
            }
        }
    }

    public Point2D.Double getPoint(int i) {
        return new Point2D.Double(xPoints[i], yPoints[i]);
    }

    public Point2D.Double[] getPoints() {
        Point2D.Double[] points = new Point2D.Double[3];
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] = new Point2D.Double(xPoints[i], yPoints[i]);
        }
        return points;
    }

}
