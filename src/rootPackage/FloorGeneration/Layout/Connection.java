package rootPackage.FloorGeneration.Layout;

import rootPackage.Direction;
import rootPackage.FloatEquivalence;
import rootPackage.Main;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A class representing a connection between two points.
 *
 * @author William Owens
 * @version 1.0
 */
public class Connection implements Comparable<Connection> {

    private MyPoint2D origin;
    private MyPoint2D destination;
    private double weight;

    public Connection(MyPoint2D origin, MyPoint2D destination) {
        this.origin = origin;
        this.destination = destination;
        this.weight = Point2D.distance(origin.x, origin.y, destination.x, destination.y);
    }

    public MyPoint2D getOrigin() {
        return origin;
    }

    public MyPoint2D getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Returns the degree measurement of the angle between this connection's origin and destination.
     * Directly above the origin is 0 degrees, directly below is 180.
     * Points to the right of the origin have positive angles, those to the left have negative.
     *
     * @return angle in degrees.
     */
    public double getRelativeDegrees() {
        return (Math.atan2(destination.x - origin.x, destination.y - origin.y) * 180 / Math.PI);
    }

    /**
     * Returns the radian measurement of the angle between this connection's origin and destination.
     * Directly above the origin is 0 radians, directly below is pi.
     * Points to the right of the origin have positive angles, those to the left have negative.
     *
     * @return angle in radians.
     */
    public double getRelativeRadians() {
        return (Math.atan2(destination.x - origin.x, destination.y - origin.y));
    }

    public void printTriangleSides() {
        if (FloatEquivalence.floatEquivalence(this.getRelativeDegrees(), 0) || FloatEquivalence.floatEquivalence(this.getRelativeDegrees(), 180) || FloatEquivalence.floatEquivalence(Math.abs(this.getRelativeDegrees()), 90)) {
            System.out.println(this.toString());
            return;
        }
        Connection conVert = new Connection(
                origin,
                new MyPoint2D(origin.x, destination.y)
        );
        Connection conHor = new Connection(
                new MyPoint2D(origin.x, destination.y),
                destination
        );
        System.out.println(conVert);
        System.out.println(conHor);

    }

    public Line2D[] generateTriangleLines() {
        final int PIXELS_PER_CARTESIAN_POINT = Main.PIXELS_PER_CARTESIAN_POINT;
        if (FloatEquivalence.floatEquivalence(this.getRelativeDegrees(), 0) || FloatEquivalence.floatEquivalence(this.getRelativeDegrees(), 180) || FloatEquivalence.floatEquivalence(Math.abs(this.getRelativeDegrees()), 90)) {
            Line2D.Double line2D = new Line2D.Double(origin.x * PIXELS_PER_CARTESIAN_POINT, origin.y * PIXELS_PER_CARTESIAN_POINT, destination.x * PIXELS_PER_CARTESIAN_POINT, destination.y * PIXELS_PER_CARTESIAN_POINT);
            return new Line2D[]{line2D};
        }
        Connection conVert = new Connection(
                origin,
                new MyPoint2D(origin.x, destination.y)
        );
        Connection conHor = new Connection(
                new MyPoint2D(origin.x, destination.y),
                destination
        );
        Line2D.Double lineVert = new Line2D.Double(conVert.origin.x * PIXELS_PER_CARTESIAN_POINT, conVert.origin.y * PIXELS_PER_CARTESIAN_POINT, conVert.destination.x * PIXELS_PER_CARTESIAN_POINT, conVert.destination.y * PIXELS_PER_CARTESIAN_POINT);
        Line2D.Double lineHor = new Line2D.Double(conHor.origin.x * PIXELS_PER_CARTESIAN_POINT, conHor.origin.y * PIXELS_PER_CARTESIAN_POINT, conHor.destination.x * PIXELS_PER_CARTESIAN_POINT, conHor.destination.y * PIXELS_PER_CARTESIAN_POINT);
        return new Line2D[]{lineVert, lineHor};
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            System.out.println(this.toString() +"  not equal to  "+o.toString());
            return false;
        }
        Connection other = (Connection) o;
        if ((this.origin.equals(other.origin)) && (this.destination.equals(other.destination)) && FloatEquivalence.floatEquivalence(this.weight, other.weight)) {
            return true;
        }
        return false;
    }

    /**
     * Returns this connection in the form of a desmos polygon string.
     */
    @Override
    public String toString() {
        return "\\operatorname{polygon}\\left(\\left(%f,%f\\right),\\left(%f,%f\\right)\\right)".formatted(origin.x, origin.y, destination.x, destination.y);
    }

    @Override
    public int compareTo(Connection o) {
        if (this.equals(o)) {
            return 0;
        }

        return (this.weight > o.weight) ? 1 : -1;
    }
}
