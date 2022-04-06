package rootPackage.FloorGeneration.Layout;

/**
 * A class representing a connection between two points.
 *
 * @author William Owens
 * @version 1.0
 */
public class Connection{

    private MyPoint2D point1;
    private MyPoint2D point2;

    public Connection(MyPoint2D point1, MyPoint2D point2){
        this.point1 = point1;
        this.point2 = point2;
    }

    public MyPoint2D getPoint1() {
        return point1;
    }

    public MyPoint2D getPoint2() {
        return point2;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }
        Connection other = (Connection) o;
        if ((this.point1.equals(other.point1) || this.point1.equals(other.point2)) && (this.point2.equals(other.point1) || this.point2.equals(other.point2))) {
            return true;
        }
        return false;
    }

    /**
     * Returns this connection in the form of a desmos polygon string.
     */
    @Override
    public String toString() {
        return "\\operatorname{polygon}\\left(\\left(%f,%f\\right),\\left(%f,%f\\right)\\right)".formatted(point1.x,point1.y,point2.x,point2.y);
    }
}
