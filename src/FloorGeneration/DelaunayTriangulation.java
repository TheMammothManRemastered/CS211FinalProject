package FloorGeneration;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

// we don't need anything outside of this package accessing this stuff
class DelaunayTriangulation {

    /*
    Watson's algorithm to accomplish delauney triangulation.
    Create a super triangle outside of the original set of points
    Then, go point by point.
    On a new point:
        Check if the point is contained within the circumcircle of all existing triangles (stored in a list)
        if a triangle's circumcircle does contain the point, AND IT ISN'T THE SUPER TRIANGLE, delete the triangle from the list
        then, create new triangles by connecting the point to all previous points in previously made triangles
        end iteration
    continue doing this until all points have been visited
    now, delete every triangle with any of the three points of the super triangle, including the super triangle itself
    return the list of triangles
    */

    public static ArrayList<Triangle> triangulate(Point2D.Double... inputPoints) {
        System.out.println("triangulating");
        // determine the 'bounding box' for the set of points
        double xHi = 0d;
        double xLo = 0d;
        double yHi = 0d;
        double yLo = 0d;
        for (Point2D point : inputPoints) {
            double x = point.getX();
            double y = point.getY();
            if (x > xHi) {
                xHi = x;
            } else if (x < xLo) {
                xLo = x;
            }
            if (y > yHi) {
                yHi = y;
            } else if (y < yLo) {
                yLo = y;
            }
        }
        // give the bounding box a little wiggle room
        xHi++;
        xLo--;
        yHi++;
        yLo--;

        System.out.println("bounding box made");

        // generate the super triangle
        Triangle superTriangle = new Triangle();
        double rectVerticalSize = yHi - yLo;
        double extraSideLengthHorizontal = rectVerticalSize / Math.sqrt(3d);
        double triangleSideLength = extraSideLengthHorizontal + (xHi - xLo) + extraSideLengthHorizontal;
        double triangleHeight = triangleSideLength * Math.sin(Math.PI / 3);
        superTriangle.addPoint(xLo - extraSideLengthHorizontal, yLo);
        superTriangle.addPoint(xHi + extraSideLengthHorizontal, yLo);
        superTriangle.addPoint((((xHi - xLo) / 2.0) + xLo), triangleHeight + yLo);

        System.out.println("super triangle constructed");

        ArrayList<Triangle> triangles = new ArrayList<Triangle>();
        // iteration one is different from the others, do it outside the loop
        Point2D.Double initialPoint = inputPoints[0];
        for (int i = 0; i < 3; i++) {
            Triangle tri = new Triangle();
            tri.addPoint(initialPoint);
            tri.addPoint(superTriangle.getPoint(i));
            tri.addPoint(superTriangle.getPoint(
                    (i+1 == 3) ? 0 : i+1
            ));
            triangles.add(tri);
        }


        for (Triangle tri : triangles) {
            System.out.println(tri);
        }

        return triangles;

    }
}
