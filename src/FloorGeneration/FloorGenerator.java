package FloorGeneration;

//TODO: totally overhauling floor generation from drunk walker method
//TODO: current idea: spawn random points, make them into a mesh somehow (delaunay triangulation?)
//TODO: from that mesh, use prim's algorithm to generate a MST.
//TODO: from there, points and edges can be converted to rooms and hallways

import java.awt.*;
import java.util.Arrays;

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
        Polygon polygon = new Polygon();
        polygon.addPoint(0,1);
        polygon.addPoint(3,4);
        polygon.addPoint(2,3);
        System.out.println(Arrays.toString(polygon.xpoints));
        System.out.println(Arrays.toString(polygon.ypoints));
        System.out.println(polygon.npoints);
        // a polygon internally has x coordinates, y coordinates in separate arrays
        // n points variable contains how many points the polygon actually has
        // the x and y arrays may be larger than n points, this is probably for optimizaion
        // only the first n points elements in these arrays are actual points in the polygon
    }
}
