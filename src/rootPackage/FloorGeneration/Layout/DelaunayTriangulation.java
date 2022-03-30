package rootPackage.FloorGeneration.Layout;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// we don't need anything outside of this package accessing this stuff

/**
 * A class containing static methods to convert an array of unconnected points into a mesh of triangles.
 * Uses a method known as Delaunay Triangulation, which, in turn, uses an algorithm called the Watson algorithm.
 *
 * @author William Owens
 * @version 2.1
 */
public class DelaunayTriangulation {

    private Point2D[] inputPoints;

    private int numPoints;
    private double[] xPoints;
    private double[] yPoints;
    private int[][] vertices;
    private int[][] adjTriangles;
    private int[] bin;

    // using Wattson's algorithm, do the numbers

    //TODO: i found a paper on this, and a video that seems good
    // redo the entire algorithm
    //TODO: this is the redo

    /**
     * Constructor.
     *
     * @param inputPoints The point cloud this DelaunayTriangulation will work with.
     */
    public DelaunayTriangulation(Point2D... inputPoints) {
        // make sure this object doesn't mess with any other function's arrays
        this.inputPoints = Arrays.copyOf(inputPoints, inputPoints.length);

        this.numPoints = inputPoints.length;

        this.xPoints = new double[numPoints+3];
        this.yPoints = new double[numPoints+3];

        this.vertices = new int[2*numPoints+1][3];
        this.adjTriangles = new int[2*numPoints+1][3];

        this.bin = new int[numPoints];

        // set the points of the super triangle
        for (int i = 0; i < vertices.length; i++) {
            this.vertices[0][i] = numPoints+i+1;
        }
        for (int i = 0; i < adjTriangles.length; i++) {
            this.adjTriangles[0][i] = 0;
        }
        this.xPoints[xPoints.length-3] = -100;
        this.yPoints[yPoints.length-3] = -100;
        this.xPoints[xPoints.length-2] = 100;
        this.yPoints[yPoints.length-2] = -100;
        this.xPoints[xPoints.length-1] = 0;
        this.yPoints[yPoints.length-1] = 100;


    }

    /**
     * Finds a delaunay triangulation of a given array of points using the method described in S. W. Sloan's paper.
     *
     * @return An array of the triangles making up the triangulation.
     * @see <a href="https://www.newcastle.edu.au/__data/assets/pdf_file/0017/22508/13_A-fast-algorithm-for-constructing-Delaunay-triangulations-in-the-plane.pdf">Sloan's paper on this algorithm</a>
     */
    public ArrayList<Triangle> triangulate() {
        // https://www.youtube.com/watch?v=pPqZPX9DvTg
        // https://www.newcastle.edu.au/__data/assets/pdf_file/0017/22508/13_A-fast-algorithm-for-constructing-Delaunay-triangulations-in-the-plane.pdf
        // (that pdf is downloaded already)
        // 1 (optional): scale the point inputs to be within 0,0 and 1,y.
        //      this improves precision
        //      y is calculated based on how much the x axis was scaled
        // 2 (optional): sort points into a grid of bins
        //      put points into a grid, then sort them based on a continuous path through that grid
        // 3: make the super triangle
        //      sloan recommends the triangle is at least 100 times the size of the point cloud
        //      if we scale it in step one, this triangle is at (0,100),(100,-100),(-100,-100)
        // 4: loop through points and find the triangles that contain each point
        //      this is different from the way I did it, this doesn't evaluate the delaunay condition to find points, not yet
        //      this checks if the point is inside a triangle, or on its edge
        //      (check video for a good diagram)
        //      to do this, get the normals for each edge of a triangle
        //      then, get the dot products of each normal and the lines between the point we're checking and the triangle's vertices
        //      if these products all have the same sign, or two have the same sign and the other is 0 (or close to it), the point is contained within that triangle
        // 4a (optional): optimize the loop
        //      instead of looping through all triangles (which is slow), check triangles closer and closer to the point
        //      this is calculated with outward facing normals and dot products
        // 5: evaluate the delaunay condition
        //      described best in the sloan paper (page 7)
        //      if we pass the condition, set swap to false, otherwise true
        // 6: if swap is true, swap
        //      can't really be described textually so easy, video at 13:40 shows it
        //      make sure adjacencies of any concerned triangles is updated (rework triangles to keep track of adjacencies)
        // 7: go through all triangles adjacent to the affected ones in a queue, and evaluate the delaunay conditions on them
        //      if they also fail the delaunay condition, repeat step 6 on them
        //      if no swap occurs and the condition is passed, do not add adjacent triangles to the queue
        // 8: repeat steps 4 through 7 on every point
        // 9: delete anyting connecting to the points on the super triangle
        // 10 (optional unless we did step 1 as well): map all the points back to their original positions

        //TODO: rework triangle (or, easier, make another triangle class)
        // triangles need to keep track of their adjacencies
        // every time the master array of triangles is updated at all, make triangles re-evaluate their adjacencies
        // this absolutely can be optimized better, only check concerned triangles?
        // make the triangle class not be just polygon 2: bad edition
        // the arrays containing points is fine, but make obtaining all points more sensical
        // constructor should take all three points at once
        // add a getNormals() function to get a triangle's normals
        // keep track of adjacencies, and make sure there's a getAdjacencies function


        // step 1: normalize points
        this.normalizePoints();

        // step 2: bin sort the input

        // step 3: create the super triangle
        //      since the super triangle is always the same, this is done in the constructor

        // step 4: this is where the fun begins!




    }

    private void normalizePoints() {
        double xMax = Double.MIN_VALUE;
        double xMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        for (Point2D point : inputPoints) {
            xMax = Math.max(xMax, point.getX());
            xMin = Math.min(xMin, point.getX());
            yMax = Math.max(yMax, point.getY());
            yMin = Math.min(yMin, point.getY());
        }
        double deltaMax = Math.max(xMax-xMin,yMax-yMin);
        // xPoints and yPoints are the same length
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (xPoints[i] - xMin) / deltaMax;
            yPoints[i] = (yPoints[i] - yMin) / deltaMax;
        }
    }

    private Triangle generateSuperTriangle() {
        return new Triangle(
                new double[]{-100, 100, 0},
                new double[]{-100, -100, 100}
        );
    }

}

