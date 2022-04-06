package rootPackage.FloorGeneration.Layout;

import java.awt.geom.Point2D;

/**
 * This class is an exact clone of the Point2D.Double class.
 * @version 2.0
 * @author William Owens
 */
public class MyPoint2D extends Point2D.Double{

    public MyPoint2D(Point2D input) {
        super(input.getX(), input.getY());
    }

    public MyPoint2D(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
