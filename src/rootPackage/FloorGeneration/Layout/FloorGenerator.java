package rootPackage.FloorGeneration.Layout;

//TODO: totally overhauling floor generation from drunk walker method
//TODO: current idea: spawn random points, make them into a mesh somehow (delaunay triangulation?)
//TODO: from that mesh, use prim's algorithm to generate a MST.
//TODO: from there, points and edges can be converted to rooms and hallways

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class FloorGenerator {
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

    public static void main(String[] args) {
        final int numPoints = 10;
        Random rng = new Random();
        ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
        for (int i = 0; i < numPoints; i++) {
            double x = rng.nextInt(21)-10;
            while (x == 0) {
                x = rng.nextInt(21)-10;
            }
            double y = rng.nextInt(21)-10;
            while (y == 0) {
                y = rng.nextInt(21)-10;
            }
            points.add(new Point2D.Double(x,y));
        }
        try {

            DelaunayTriangulation del = new DelaunayTriangulation(
                    new Point2D.Double(-2.0, -3.0),
                    new Point2D.Double(0.0, -2.0),
                    new Point2D.Double(-2.0, -1.0),
                    new Point2D.Double(2.0, 1.0),
                    new Point2D.Double(-3.0, 2.0),
                    new Point2D.Double(-1.0, 3.0),
                    new Point2D.Double(1.0, 3.0),
                    new Point2D.Double(0,-8),
                    new Point2D.Double(4.0, 4.0),
                    new Point2D.Double(5.0, -8.0),
                    new Point2D.Double(-8,0),
                    new Point2D.Double(3.0, 10.0),
                    new Point2D.Double(-0.5, 1.0),
                    new Point2D.Double(-0.7, 2.0),
                    new Point2D.Double(0,-1),
                    new Point2D.Double(0,-4),
                    new Point2D.Double(0,-6),
                    new Point2D.Double(-2,0),
                    new Point2D.Double(-5,0)
            );

            del.triangulate();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void sortPoints(ArrayList<Point2D.Double> arr) {
        int n = arr.size();
        for (int i = 1; i < n; ++i) {
            double key = arr.get(i).x;
            double y = arr.get(i).y;
            int j = i - 1;

            while (j >= 0 && arr.get(j).x > key) {
                arr.set(j + 1, arr.get(j));
                j = j - 1;
            }
            arr.set(j + 1, new Point2D.Double(key, y));
        }
    }
}
