package FloorGeneration;

import java.awt.geom.Point2D;

/**
 * The circle class contains data about a circle. It can also check if a given coordinate is inside the circle.
 */
class Circle {

    private double centerX;
    private double centerY;
    private double radius;

    /**
     * Constructor.
     * @param centerX The x component of the center point.
     * @param centerY The y component of the center point.
     * @param radius The radius of the circle.
     */
    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    /**
     * Checks if the circle contains a point.
     * 'Contains', in this context, means points fully within the circle, and not those lying on the circle's edge.
     * @param x The x component of the point.
     * @param y The y component of the point.
     * @return True or False.
     */
    public boolean contains(double x, double y) {
        return (Math.sqrt(Math.pow(x-centerX,2) + Math.pow(y-centerY,2)) < radius);
    }

    /**
     * Checks if the circle contains a point.
     * 'Contains', in this context, means points fully within the circle, and not those lying on the circle's edge.
     * @param point The point.
     * @return True or False.
     */
    public boolean contains(Point2D.Double point) {
        double x = point.getX();
        double y = point.getY();
        return (Math.sqrt(Math.pow(x-centerX,2) + Math.pow(y-centerY,2)) < radius);
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "\\left(x-%f\\right)^{2}+\\left(y-%f\\right)^{2}=%f^{2}".formatted(centerX,centerY,radius);
    }
}
