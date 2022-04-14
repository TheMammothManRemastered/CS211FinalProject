package rootPackage.FloorGeneration.Layout;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * A class containing static methods to convert an array of unconnected points into a mesh of triangles.
 * Uses a method known as Delaunay Triangulation, a triangulation method created by Boris Delaunay in 1934.
 * This particular implementation uses the algorithm described by S.W. Sloan in Advanced English Software, Volume 9, Number 1.
 *
 * @author William Owens
 * @version 3.1
 */

//TODO: this works well and correctly, but it doesn't implement the bin sort stage. this is probably fine, but change that if there is time
public class DelaunayTriangulation {

    private final Point2D[] inputPoints;
    private final MyPoint2D[] points;
    private final int[][] verticesOfTriangles;
    private final int[][] adjTriangles;

    private int numPoints;
    private double xMin;
    private double yMin;
    private double deltaMax;

    /**
     * Constructor.
     *
     * @param inputPoints The point cloud this DelaunayTriangulation will work with.
     */
    public DelaunayTriangulation(Point2D... inputPoints) {
        this.inputPoints = inputPoints;

        this.numPoints = inputPoints.length;

        this.points = new MyPoint2D[numPoints + 3];

        this.verticesOfTriangles = new int[2 * numPoints + 1][3];
        this.adjTriangles = new int[2 * numPoints + 1][3];
    }

    /**
     * Finds a delaunay triangulation of a given array of points using the method described in S. W. Sloan's paper.
     *
     * @return An array of the triangles making up the triangulation.
     * @see <a href="https://www.newcastle.edu.au/__data/assets/pdf_file/0017/22508/13_A-fast-algorithm-for-constructing-Delaunay-triangulations-in-the-plane.pdf">Sloan's paper on this algorithm</a>
     */
    public Triangulation triangulate() {
        // https://www.newcastle.edu.au/__data/assets/pdf_file/0017/22508/13_A-fast-algorithm-for-constructing-Delaunay-triangulations-in-the-plane.pdf
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
        //      can't really be described textually so easy, but basically remake and rotate two adjacent triangles
        //      make sure adjacencies of any concerned triangles is updated (rework triangles to keep track of adjacencies)
        // 7: go through all triangles adjacent to the affected ones in a queue, and evaluate the delaunay conditions on them
        //      if they also fail the delaunay condition, repeat step 6 on them
        //      if no swap occurs and the condition is passed, do not add adjacent triangles to the queue
        // 8: repeat steps 4 through 7 on every point
        // 9: delete anyting connecting to the points on the super triangle
        // 10 (optional unless we did step 1 as well): map all the points back to their original positions

        // step 1: normalize points
        // this does not affect the original input, only the xPoints and yPoints arrays
        for (int i = 0; i < numPoints; i++) {
            points[i] = new MyPoint2D(inputPoints[i]);
        }
        this.normalizePoints();

        // step 3: create the super triangle (skipping bin sort, it's unnecessary for functionality. might add it later if there's time)
        createSuperTriangle();

        // steps 4-8: this is where the fun begins!
        int numTriangles = 1;
        int indexOfLastTriangleCreated = 0; // the super triangle is the first created, and is in the first index of triangles

        for (int pointIndex = 0; pointIndex < numPoints - 3; pointIndex++) {

            // find triangle containing current point
            indexOfLastTriangleCreated = findTriangleContainingPoint(points[pointIndex], indexOfLastTriangleCreated);
            // if a triangle is found that contains the current point (which will always be the case), its index is stored in indexOfLastTriangleCreated
            numTriangles += 2;
            // delete this triangle, replace with two new ones
            splitTriangle(numTriangles, indexOfLastTriangleCreated, pointIndex);
            // new triangles are set up
            // the delaunay condition on these triangles is most likely not met, now is the time to fix that
            // put all the triangles that contain the current point into a LIFO stack, if they have an adjacency on the point's opposing edge
            Stack<Integer> triangleStack = new Stack<>(); // just for clarity, this holds triangle indices
            if (adjTriangles[indexOfLastTriangleCreated][1] >= 0) {
                triangleStack.add(indexOfLastTriangleCreated);
            }
            if (adjTriangles[numTriangles - 2][1] >= 0) {
                triangleStack.add(numTriangles - 2);
            }
            if (adjTriangles[numTriangles - 1][1] >= 0) {
                triangleStack.add(numTriangles - 1);
            }
            // while there are still elements in the stack, iterate
            while (triangleStack.size() > 0) {
                // the triangle we are currently on (as seen in the Sloan paper figure 7)
                int triangleL = triangleStack.pop();
                // all of this is defined as in figure 7
                MyPoint2D vertex1 = new MyPoint2D(points[verticesOfTriangles[triangleL][2]].x, points[verticesOfTriangles[triangleL][2]].y);
                MyPoint2D vertex2 = new MyPoint2D(points[verticesOfTriangles[triangleL][1]].x, points[verticesOfTriangles[triangleL][1]].y);

                int oppositeVertex = -1;
                int oppositeVertexID = -1;

                // set opposite vertex, opposite meaning vertex 3 in this case
                for (int n = 0; n < 3; n++) {
                    if ((verticesOfTriangles[adjTriangles[triangleL][1]][n] != verticesOfTriangles[triangleL][1]) &&
                            (verticesOfTriangles[adjTriangles[triangleL][1]][n] != verticesOfTriangles[triangleL][2])) {
                        oppositeVertex = verticesOfTriangles[adjTriangles[triangleL][1]][n];
                        oppositeVertexID = n;
                        break;
                    }
                }

                // now, we can set up vertex 3
                MyPoint2D vertex3 = new MyPoint2D(points[oppositeVertex].x, points[oppositeVertex].y);
                // P being the "current" point
                MyPoint2D P = new MyPoint2D(points[pointIndex].x, points[pointIndex].y);

                // circumcircle check for P in triangle R (v1,v2,v3)
                double cosA = ((vertex1.x - vertex3.x) * (vertex2.x - vertex3.x) + (vertex1.y - vertex3.y) * (vertex2.y - vertex3.y));
                double cosB = ((vertex2.x - P.x) * (vertex1.x - P.x) + (vertex2.y - P.y) * (vertex1.y - P.y));

                // horrible, terrible, awfully long conditional
                boolean circumcircleContains = (((cosA < 0) && (cosB < 0)) || ((-cosA * ((vertex2.x - P.x) * (vertex1.y - P.y) - (vertex2.y - P.y) * (vertex1.x - P.x))) >
                        (cosB * ((vertex1.x - vertex3.x) * (vertex2.y - vertex3.y) - (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x)))));

                if (circumcircleContains) {
                    // delaunay condition has failed for this point, swap the diagonal between triangles L and R
                    // set up triangles
                    int triangleR = adjTriangles[triangleL][1];
                    int triangleC = adjTriangles[triangleL][2];
                    int triangleA = adjTriangles[triangleR][(oppositeVertexID + 2) % 3];
                    // set up adjacencies for A
                    if (triangleA >= 0) {
                        for (int n = 0; n < 3; n++) {
                            if (adjTriangles[triangleA][n] == triangleR) {
                                adjTriangles[triangleA][n] = triangleL;
                                break;
                            }
                        }
                    }
                    // set up adjacencies for C
                    if (triangleC >= 0) {
                        for (int n = 0; n < 3; n++) {
                            if (adjTriangles[triangleC][n] == triangleL) {
                                adjTriangles[triangleC][n] = triangleR;
                                break;
                            }
                        }
                    }
                    // triangle B doesn't actually change adjacencies in this setup
                    // shift triangle R
                    for (int n = 0; n < 3; n++)
                        if (verticesOfTriangles[triangleR][n] == oppositeVertex) {
                            verticesOfTriangles[triangleR][(n + 2) % 3] = pointIndex;
                            break;
                        }
                    for (int n = 0; n < 3; n++)
                        if (adjTriangles[triangleR][n] == triangleL) {
                            adjTriangles[triangleR][n] = triangleC;
                            break;
                        }
                    for (int n = 0; n < 3; n++)
                        if (adjTriangles[triangleR][n] == triangleA) {
                            adjTriangles[triangleR][n] = triangleL;
                            break;
                        }
                    for (int n = 0; n < 3; n++)
                        if (verticesOfTriangles[triangleR][0] != pointIndex) {
                            int temp1 = verticesOfTriangles[triangleR][0];
                            int temp2 = adjTriangles[triangleR][0];
                            // vertices
                            verticesOfTriangles[triangleR][0] = verticesOfTriangles[triangleR][1];
                            verticesOfTriangles[triangleR][1] = verticesOfTriangles[triangleR][2];
                            verticesOfTriangles[triangleR][2] = temp1;
                            // adjacencies
                            adjTriangles[triangleR][0] = adjTriangles[triangleR][1];
                            adjTriangles[triangleR][1] = adjTriangles[triangleR][2];
                            adjTriangles[triangleR][2] = temp2;
                        }

                    // shift triangle L
                    verticesOfTriangles[triangleL][2] = oppositeVertex;
                    for (int m = 0; m < 3; m++)
                        if (adjTriangles[triangleL][m] == triangleC) {
                            adjTriangles[triangleL][m] = triangleR;
                            break;
                        }
                    for (int m = 0; m < 3; m++)
                        if (adjTriangles[triangleL][m] == triangleR) {
                            adjTriangles[triangleL][m] = triangleA;
                            break;
                        }

                    // if L or R have triangles opposite P in them, add them to the stack
                    if (adjTriangles[triangleL][1] >= 0) {
                        triangleStack.add(triangleL);
                    }
                    if (adjTriangles[triangleR][1] >= 0) {
                        triangleStack.add(triangleR);
                    }
                }


                /*
                FloorLayoutGenerator.delaunayStagesSb.append("Delaunay triangulation in progress - current state of triangles: \n");
                for (int i = 0; i < verticesOfTriangles.length; i++) {
                    int[] trianglePoints = verticesOfTriangles[i];
                    MyPoint2D point1 = points[trianglePoints[0]];
                    MyPoint2D point2 = points[trianglePoints[1]];
                    MyPoint2D point3 = points[trianglePoints[2]];
                    if (point1.equals(point2)) {
                        continue;
                    }
                    FloorLayoutGenerator.delaunayStagesSb.append("\\operatorname{polygon}\\left(\\left(%f,%f\\right),\\left(%f,%f\\right),\\left(%f,%f\\right)\\right)%n".formatted(point1.x,point1.y,point2.x,point2.y,point3.x,point3.y));
                }
                FloorLayoutGenerator.delaunayStagesSb.append("\n");


                 */
            }

        }

        // step 9: kill anything related to the supertriangle
        int[][] finalVerticesOfTriangles = removeSuperTriangleFromPointsSet(numTriangles);

        // step 10: reverse the mapping of points to the area near (0,0)
        numPoints -= 3;
        deNormalizePoints();

        //TODO: debug code for desmos, delete this when it is no longer needed
        FloorLayoutGenerator.delaunayStagesSb.append("Final Delaunay Triangulation\n");
        for (int[] finalVerticesOfTriangle : finalVerticesOfTriangles) {
            MyPoint2D point1 = points[finalVerticesOfTriangle[0]];
            MyPoint2D point2 = points[finalVerticesOfTriangle[1]];
            MyPoint2D point3 = points[finalVerticesOfTriangle[2]];
            FloorLayoutGenerator.delaunayStagesSb.append("\\operatorname{polygon}\\left(\\left(%f,%f\\right),\\left(%f,%f\\right),\\left(%f,%f\\right)\\right)%n".formatted(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y));
        }
        FloorLayoutGenerator.delaunayStagesSb.append("\n");

        // algorithm completed, translate into a usable output and return
        return parseTriangulation(finalVerticesOfTriangles);
    }

    private int[][] removeSuperTriangleFromPointsSet(int numTriangles) {
        int numDeadTriangles = 0;
        int[] deadTriangles = new int[numTriangles]; // 0 if triangle is alive, 1 if it's dead
        for (int i = 0; i < numTriangles; i++) {
            if ((verticesOfTriangles[i][0] >= (numPoints - 3)) ||
                    (verticesOfTriangles[i][1] >= (numPoints - 3)) ||
                    (verticesOfTriangles[i][2] >= (numPoints - 3))) {
                deadTriangles[i] = 1;
                numDeadTriangles++;
            }
        }
        int numTrianglesFinal = numTriangles - numDeadTriangles;

        int[][] finalVerticesOfTriangles = new int[numTrianglesFinal * 3][3];
        int index = 0;
        for (int i = 0; i < numTriangles; i++) {
            if (!(deadTriangles[i] == 1)) {
                finalVerticesOfTriangles[index][0] = verticesOfTriangles[i][0];
                finalVerticesOfTriangles[index][1] = verticesOfTriangles[i][1];
                finalVerticesOfTriangles[index][2] = verticesOfTriangles[i][2];
                index++;
            }
        }
        return finalVerticesOfTriangles;
    }

    /**
     * Splits a given triangle into 3, dealing with adjacencies and the like
     * @param splitPoint the point about which the triangle is split
     */
    private void splitTriangle(int numTriangles, int indexOfSplitTriangle, int splitPoint) {
        // the two new triangles
        verticesOfTriangles[numTriangles - 2][0] = splitPoint;
        verticesOfTriangles[numTriangles - 2][1] = verticesOfTriangles[indexOfSplitTriangle][1];
        verticesOfTriangles[numTriangles - 2][2] = verticesOfTriangles[indexOfSplitTriangle][2];
        verticesOfTriangles[numTriangles - 1][0] = splitPoint;
        verticesOfTriangles[numTriangles - 1][1] = verticesOfTriangles[indexOfSplitTriangle][2];
        verticesOfTriangles[numTriangles - 1][2] = verticesOfTriangles[indexOfSplitTriangle][0];
        // original triangle gets replaced later
        // update adjacencies
        // first, adjacencies of the triangles that were once around the one we just edited
        // adjacency 1 won't change from this operation
        int adjacency2 = adjTriangles[indexOfSplitTriangle][1];
        int adjacency3 = adjTriangles[indexOfSplitTriangle][2];
        updateSurroundingTriangleAdjacencies(numTriangles, indexOfSplitTriangle, adjacency2, 2);
        updateSurroundingTriangleAdjacencies(numTriangles, indexOfSplitTriangle, adjacency3, 1);
        // next, set up adjacencies of the newly made triangles
        setUpSplitTriangleAdjacencies(numTriangles, indexOfSplitTriangle);
        // now we update the old first triangle, it's no longer up to date
        // vertices
        verticesOfTriangles[indexOfSplitTriangle][2] = verticesOfTriangles[indexOfSplitTriangle][1];
        verticesOfTriangles[indexOfSplitTriangle][1] = verticesOfTriangles[indexOfSplitTriangle][0];
        verticesOfTriangles[indexOfSplitTriangle][0] = splitPoint;
        // adjacencies
        adjTriangles[indexOfSplitTriangle][1] = adjTriangles[indexOfSplitTriangle][0];
        adjTriangles[indexOfSplitTriangle][2] = numTriangles - 2;
        adjTriangles[indexOfSplitTriangle][0] = numTriangles - 1;
    }

    /**
     * Updates the adjacencies of a given triangle that was just split into multiple
     * @param numTriangles the current value of numTriangles //TODO: make this global
     * @param indexOfSplitTriangle the index of the triangle that was recently split
     * @param adjacency the index of the triangle that needs its adjacencies updated
     * @param offset an offset used in calculating which triangle this one is now next to (2 for adjacency 2, 1 for adjacency 3)
     */
    private void updateSurroundingTriangleAdjacencies(int numTriangles, int indexOfSplitTriangle, int adjacency, int offset) {
        // if it's -1, then there was no triangle next to it anyway. there certainly won't be one now.
        if (adjacency >= 0) {
            for (int m = 0; m < 3; m++) {
                if (adjTriangles[adjacency][m] == indexOfSplitTriangle) {
                    adjTriangles[adjacency][m] = numTriangles - offset;
                    break;
                }
            }
        }
    }

    /**
     * Sets up the triangle adjacencies for a triangle that was split
     */
    private void setUpSplitTriangleAdjacencies(int numTriangles, int indexOfSplitTriangle) {
        adjTriangles[numTriangles - 2][0] = indexOfSplitTriangle;
        adjTriangles[numTriangles - 2][1] = adjTriangles[indexOfSplitTriangle][1];
        adjTriangles[numTriangles - 2][2] = numTriangles - 1;
        adjTriangles[numTriangles - 1][0] = numTriangles - 2;
        adjTriangles[numTriangles - 1][1] = adjTriangles[indexOfSplitTriangle][2];
        adjTriangles[numTriangles - 1][2] = indexOfSplitTriangle;
    }

    /**
     * Sets up the super triangle's data. (big triangle at (-100,-100) (100,-100) and (0,100))
     */
    private void createSuperTriangle() {
        for (int i = 0; i < 3; i++) {
            this.verticesOfTriangles[0][i] = numPoints + i;
        }
        for (int i = 0; i < 3; i++) {
            this.adjTriangles[0][i] = -1;
        }
        // super triangle is stored in the last 3 indices of x and y points
        this.points[numPoints] = new MyPoint2D(-100, -100);
        this.points[numPoints + 1] = new MyPoint2D(100, -100);
        this.points[numPoints + 2] = new MyPoint2D(0, 100);
        numPoints += 3;
    }

    /**
     * Helper for translating the output of triangulation into something useful.
     * Returns an array of two arraylists. The first one contains all the points in the triangulation.
     * The second contains all the connections in the triangulation.
     */
    private Triangulation parseTriangulation(int[][] trianglePoints) {

        ArrayList<Connection> connections = new ArrayList<>();
        ArrayList<MyPoint2D> outputPoints = new ArrayList<>();

        // for each triangle, add its points and its edges (in both directions) to the output
        for (int[] triangle : trianglePoints) {
            outputPoints.addAll(List.of(new MyPoint2D[]{points[triangle[0]], points[triangle[1]], points[triangle[2]]}));
            connections.addAll(List.of(generateTwoWayConnectionsFromTriangle(new MyPoint2D[]{points[triangle[0]], points[triangle[1]], points[triangle[2]]})));
        }

        return new Triangulation(outputPoints, connections);
    }

    /**
     * Generates six connections representing the edges of a triangle using the vertices of said triangle.
     * Since connections are 1 directional, each side of the triangle is represented as two overlapping connections facing in opposite directions.
     *
     * @param inputPoints An array of three points representing a triangle. The array MUST be of size 3.
     * @return An array of six connections.
     */
    private Connection[] generateTwoWayConnectionsFromTriangle(MyPoint2D[] inputPoints) {
        Connection[] output = new Connection[6];
        for (int i = 0; i < 6; i += 2) {
            System.out.println("i " + i);
            System.out.println("i+2 " + ((i + 2) / 2.0));
            Connection[] twoWayEdge = generateTwoWayConnection(
                    inputPoints[i / 2],
                    inputPoints[(i + 2 == 6) ? 0 : ((i + 2) / 2)]
            );
            output[i] = twoWayEdge[0];
            output[i + 1] = twoWayEdge[1];
        }
        return output;
    }

    /**
     * Generates two overlapping connections facing in opposite directions using two points
     */
    private Connection[] generateTwoWayConnection(MyPoint2D origin, MyPoint2D destination) {
        Connection c1 = new Connection(origin, destination);
        Connection c2 = new Connection(destination, origin);
        return new Connection[]{c1, c2};
    }

    /**
     * Iterates through triangles and finds one that contains the given point.
     * Doesn't iterate through every single triangle, is "smart".
     *
     * @param pointToFind           The point to find.
     * @param startingTriangleIndex The triangle to start the search at.
     * @return The index of the triangle containing the point.
     */
    private int findTriangleContainingPoint(MyPoint2D pointToFind, int startingTriangleIndex) {

        boolean triangleFound = false;

        int currentTriangleIndex = startingTriangleIndex;

        int[] currentTriangleVertices = verticesOfTriangles[currentTriangleIndex];
        int[] currentTriangleAdjacencies = adjTriangles[currentTriangleIndex];
        while (!triangleFound) {
            for (int sideNum = 0; sideNum < 3; sideNum++) {
                // two 3d vectors, one being the side of the triangle we're looking at, the other being one formed with
                // the point we're looking for and the first point of the side we're looking at
                double[] sideVector = new double[3];
                double[] pointVector = new double[3];
                double crossProduct;
                MyPoint2D sidePoint1 = points[currentTriangleVertices[sideNum]];
                MyPoint2D sidePoint2 = points[currentTriangleVertices[(sideNum + 1 == 3) ? 0 : sideNum + 1]];
                sideVector[0] = sidePoint2.x - sidePoint1.x;
                sideVector[1] = sidePoint2.y - sidePoint1.y;
                sideVector[2] = 0; // no z component, this is a 2d plane
                pointVector[0] = pointToFind.x - sidePoint1.x;
                pointVector[1] = pointToFind.y - sidePoint1.y;
                pointVector[2] = 0; // again, no z component
                // take the cross product of side X point, only care about the z component
                double[][] matrix = new double[2][2];
                matrix[0][0] = sideVector[0];
                matrix[0][1] = sideVector[1];
                matrix[1][0] = pointVector[0];
                matrix[1][1] = pointVector[1];
                // determinant (ad - bc)
                crossProduct = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));

                // if the cross product is negative, then the point is to the right of this triangle, and therefore not inside
                if (crossProduct < 0) {
                    currentTriangleIndex = currentTriangleAdjacencies[sideNum];
                    // if the adjacency isn't pointing at anything, then go backwards until we reach one that does exist
                    while (currentTriangleIndex == -1) {
                        currentTriangleIndex = currentTriangleAdjacencies[(sideNum == 0) ? 2 : sideNum - 1];
                    }
                    currentTriangleVertices = verticesOfTriangles[currentTriangleIndex];
                    currentTriangleAdjacencies = adjTriangles[currentTriangleIndex];
                    triangleFound = false;
                    break;
                } else {
                    triangleFound = true;
                }
            }
        }
        return currentTriangleIndex;
    }

    /**
     * Normalizes the contents of points.
     * Normalizing, in this context, means scaling the points down into a smaller area near (0,0).
     * This allows better advantage to be taken of float precision.
     */
    private void normalizePoints() {
        double xMax = Double.MIN_VALUE;
        xMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;
        yMin = Double.MAX_VALUE;
        for (Point2D point : inputPoints) {
            xMax = Math.max(xMax, point.getX());
            xMin = Math.min(xMin, point.getX());
            yMax = Math.max(yMax, point.getY());
            yMin = Math.min(yMin, point.getY());
        }
        deltaMax = Math.max(xMax - xMin, yMax - yMin);
        // xPoints and yPoints are the same length
        for (int i = 0; i < points.length - 3; i++) {
            MyPoint2D point = points[i];
            point.x = (point.x - xMin) / deltaMax;
            point.y = (point.y - yMin) / deltaMax;
        }
    }

    /**
     * Reverses the effects of the normalizePoints() method.
     */
    private void deNormalizePoints() {
        for (int i = 0; i < numPoints; i++) {
            points[i].x = points[i].x * deltaMax + xMin;
            points[i].y = points[i].y * deltaMax + yMin;
        }
    }

}

