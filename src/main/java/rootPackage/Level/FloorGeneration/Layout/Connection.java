package rootPackage.Level.FloorGeneration.Layout;

import org.json.simple.*;
import rootPackage.FloatEquivalence;

import java.awt.geom.Point2D;

/**
 * A class representing a directional, weighted connection between two points.
 *
 * @author William Owens
 * @version 1.2
 */
public class Connection implements Comparable<Connection> {

    private final MyPoint2D origin;
    private final MyPoint2D destination;
    private final double weight;

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
     * Checks if the connection's origin or destination are equal to a given point.
     */
    public boolean contains(MyPoint2D p) {
        return (p.equals(origin) || p.equals(destination));
    }

    /**
     * Checks if the connection's origin or destination are equal to any of the given points.
     */
    public boolean containsAny(MyPoint2D[] ps) {
        for (MyPoint2D p : ps) {
            if (this.contains(p)) {
                return true;
            }
        }
        return false;
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
     * Returns this connection with the destination and origin points swapped.
     */
    public Connection opposite() {
        return new Connection(destination, origin);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }
        Connection other = (Connection) o;
        return (this.origin.equals(other.origin)) && (this.destination.equals(other.destination)) && FloatEquivalence.equals(this.weight, other.weight);
    }

    /**
     * Returns this connection in the form of a desmos polygon string, should really only be used for debug.
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
