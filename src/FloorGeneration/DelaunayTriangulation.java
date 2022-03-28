package FloorGeneration;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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

    public static ArrayList<Path2D.Double> triangulate(Point2D.Double... inputPoints) {
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

        // generate the super triangle
        // TODO: Path2D kinda sucks, making my own Triangle class may be easier
        // TODO: can contain a circumcircle method
        // TODO: contains the points
        
        Path2D.Double superTriangle = new Path2D.Double();
        double rectVerticalSize = yHi - yLo;
        double extraSideLengthHorizontal = rectVerticalSize / Math.sqrt(3d);
        double triangleSideLength = extraSideLengthHorizontal + (xHi - xLo) + extraSideLengthHorizontal;
        double triangleHeight = triangleSideLength * Math.sin(Math.PI / 3);
        superTriangle.moveTo(xLo - extraSideLengthHorizontal, yLo);
        superTriangle.lineTo(xHi + extraSideLengthHorizontal, yLo);
        superTriangle.lineTo((((xHi - xLo) / 2.0) + xLo), triangleHeight + yLo);
        superTriangle.closePath();

        superTriangle.getPathIterator();

    }
}
