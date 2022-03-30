package rootPackage.FloorGeneration.Layout;

import java.awt.geom.Point2D;

class MyPoint2D extends Point2D.Double implements Comparable<MyPoint2D>{

    private long binNumber;

    public MyPoint2D(double x, double y) {
        super(x, y);
    }

    public MyPoint2D(double x, double y, long binNumber) {
        super(x, y);
        this.binNumber = binNumber;
    }

    public long getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(long binNumber) {
        this.binNumber = binNumber;
    }

    @Override
    public int compareTo(MyPoint2D o) {
        if (this.binNumber > o.binNumber) {
            return 1;
        }
        else if (this.binNumber < o.binNumber) {
            return -1;
        }
        return 0;
    }
}
