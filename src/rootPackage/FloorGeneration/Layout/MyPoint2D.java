package rootPackage.FloorGeneration.Layout;

import rootPackage.FloatEquivalence;

import java.awt.geom.Point2D;

/**
 * This class is an exact clone of the Point2D.Double class, save for its toString, which outputs the point in the form that desmos uses.
 * Also, this one is comparable.
 *
 * @version 2.5
 * @author William Owens
 */
public class MyPoint2D extends Point2D.Double implements Comparable<MyPoint2D>{

    public MyPoint2D(Point2D input) {
        super(input.getX(), input.getY());
    }

    public MyPoint2D(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "\\left(%f,%f\\right)".formatted(this.x,this.y);
    }

    @Override
    public int compareTo(MyPoint2D o) {
        // lower y first, then lower x
        if (FloatEquivalence.equals(this.y, o.y)) {
            if (FloatEquivalence.equals(this.x, o.x)) {
                return 0;
            }
            else if (o.x < this.x) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else if (o.y < this.y) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
