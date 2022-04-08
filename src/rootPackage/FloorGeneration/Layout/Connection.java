package rootPackage.FloorGeneration.Layout;

import rootPackage.FloatEquivalence;

import java.awt.geom.Point2D;

/**
 * A class representing a connection between two points.
 *
 * @author William Owens
 * @version 1.0
 */
public class Connection implements Comparable<Connection>{

    private MyPoint2D origin;
    private MyPoint2D destination;
    private double weight;

    public Connection(MyPoint2D origin, MyPoint2D destination){
        this.origin = origin;
        this.destination = destination;
        this.weight = Point2D.distance(origin.x,origin.y,destination.x,destination.y);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
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
