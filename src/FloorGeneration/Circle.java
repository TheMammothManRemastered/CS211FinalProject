package FloorGeneration;

import java.awt.geom.Point2D;

/**
 * The circle class contains data about a circle. It can also check if a given coordinate is inside the circle.
 */
class Circle {

    private double centerX;
    private double centerY;
    private double radius;

    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    public boolean contains(double x, double y) {
        return (Math.sqrt(Math.pow(x-centerX,2) + Math.pow(y-centerY,2)) < radius);
    }

    public boolean contains(Point2D.Double point) {
        double x = point.getX();
        double y = point.getY();
        return (Math.sqrt(Math.pow(x-centerX,2) + Math.pow(y-centerY,2)) < radius);
    }
}
