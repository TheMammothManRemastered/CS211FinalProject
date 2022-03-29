package FloorGeneration;

import java.awt.geom.Point2D;

/**
 * A class containing a cartesian Point2D.Double and its polar representation.
 * A polar coordinate is a coordinate stored in the form of a length and an angle relative to an origin point, rather than an X and a Y component.
 * This class has the ability to use any cartesian point as its origin, not just (0,0).
 */
class PolarPoint implements Comparable<PolarPoint> {

    private Point2D.Double cartesianPoint;
    private double angle;
    private double radius;

    /**
     * Constructor. Uses a given origin point as its reference point.
     * @param cartesianPoint The cartesian point to be converted.
     * @param originPoint The point to use as a reference point.
     */
    public PolarPoint(Point2D.Double cartesianPoint, Point2D.Double originPoint) {
        //TODO: this method calculates points around the origin (0,0)
        //TODO: change that to work with a given origin point instead
        this.cartesianPoint = cartesianPoint;
        double modifiedX = cartesianPoint.x - originPoint.x;
        double modifiedY = cartesianPoint.y - originPoint.y;
        this.radius = Math.sqrt(Math.pow(modifiedX, 2.0) + Math.pow(modifiedY, 2.0));
        this.angle = ((Math.acos(modifiedX / this.radius)) * 180) / Math.PI;
        // this angle calculation is wrong, so it needs extra logic
        if (modifiedX > 0 && modifiedY < 0) {
            this.angle += 270;
        } else if (modifiedX < 0 && modifiedY < 0) {
            this.angle += 90;
        } else if ((Triangle.floatEquivalence(modifiedX, 0)) && modifiedY < 0) {
            this.angle += 180;
        } else if ((Triangle.floatEquivalence(modifiedY, 0.0)))
            if (modifiedX < 0) {
                this.angle = 180.0;
            } else {
                this.angle = 0.0;
            }
    }

    /**
     * Constructor. Uses the origin (0,0) as its reference point.
     * @param cartesianPoint The cartesian point to be converted.
     */
    public PolarPoint(Point2D.Double cartesianPoint) {
        this.cartesianPoint = cartesianPoint;
        double modifiedX = cartesianPoint.x;
        double modifiedY = cartesianPoint.y;
        this.radius = Math.sqrt(Math.pow(modifiedX, 2.0) + Math.pow(modifiedY, 2.0));
        this.angle = ((Math.acos(modifiedX / this.radius)) * 180) / Math.PI;
        // this angle calculation is wrong, so it needs extra logic
        if (modifiedX > 0 && modifiedY < 0) {
            this.angle += 270;
        } else if (modifiedX < 0 && modifiedY < 0) {
            this.angle += 90;
        } else if ((Triangle.floatEquivalence(modifiedX, 0)) && modifiedY < 0) {
            this.angle += 180;
        } else if ((Triangle.floatEquivalence(modifiedY, 0.0)))
            if (modifiedX < 0) {
                this.angle = 180.0;
            } else {
                this.angle = 0.0;
            }
    }

    public Point2D.Double getCartesianPoint() {
        return cartesianPoint;
    }

    public double getAngle() {
        return angle;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "angle: " + angle;
    }

    @Override
    public int compareTo(PolarPoint o) {
        if (Triangle.floatEquivalence(o.angle, this.angle)) {
            if (Triangle.floatEquivalence(o.radius, this.radius)) {
                return 0;
            } else if (o.radius > this.radius) {
                return 1;
            } else return -1;
        }
        if (o.angle - this.angle > 0) {
            return -1;
        } else if (o.angle - this.angle < 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
