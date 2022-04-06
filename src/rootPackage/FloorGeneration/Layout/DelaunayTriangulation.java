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

    private Point2D[] inputPoints;

    private int numPoints;
    private MyPoint2D[] points;
    private int[][] verticesOfTriangles;
    private int[][] adjTriangles;
    private double xMin;
    private double yMin;
    private double deltaMax;

    public static void main(String[] args) {

        // okay test time :)
        Random rng = new Random(44);

        final int POINT_CLOUD_SIZE = 20;
        MyPoint2D[] pointCloud = new MyPoint2D[POINT_CLOUD_SIZE];
        for (int i = 0; i < POINT_CLOUD_SIZE; i++) {
            int randX = rng.nextInt(40)-20;
            int randY = rng.nextInt(40)-20;
            pointCloud[i] = new MyPoint2D(randX,randY);
        }

        DelaunayTriangulation dt = new DelaunayTriangulation(pointCloud);

        ArrayList<Connection> map = dt.triangulate();

        System.out.println("le map");
        for (Connection con : map) {
            System.out.println(con);
        }

    }

    /**
     * Constructor.
     *
     * @param inputPoints The point cloud this DelaunayTriangulation will work with.
     */
    public DelaunayTriangulation(Point2D... inputPoints) {
        // make sure this object doesn't mess with any other function's arrays
        this.inputPoints = Arrays.copyOf(inputPoints, inputPoints.length);

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
    public ArrayList<Connection> triangulate() {
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

        // step 2: bin sort the input

        // step 3: create the super triangle
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
        numPoints+=3;

        // steps 4-8: this is where the fun begins!
        int numTriangles = 1;
        for (int pointIndex = 0; pointIndex < numPoints - 3; pointIndex++) {
            int indexOfLastTriangleCreated = numTriangles - 1;
            // find triangle containing this point
            indexOfLastTriangleCreated = findTriangleContainingPoint(points[pointIndex], indexOfLastTriangleCreated);
            numTriangles += 2;
            // delete this triangle, replace with two new ones
            // the two new triangles
            verticesOfTriangles[numTriangles - 2][0] = pointIndex;
            verticesOfTriangles[numTriangles - 2][1] = verticesOfTriangles[indexOfLastTriangleCreated][1];
            verticesOfTriangles[numTriangles - 2][2] = verticesOfTriangles[indexOfLastTriangleCreated][2];
            verticesOfTriangles[numTriangles - 1][0] = pointIndex;
            verticesOfTriangles[numTriangles - 1][1] = verticesOfTriangles[indexOfLastTriangleCreated][2];
            verticesOfTriangles[numTriangles - 1][2] = verticesOfTriangles[indexOfLastTriangleCreated][0];
            // original triangle gets replaced later
            // update adjacencies
            // first, adjacencies of the triangles that were once around the one we just edited
            int adjacency1 = adjTriangles[indexOfLastTriangleCreated][0];
            int adjacency2 = adjTriangles[indexOfLastTriangleCreated][1];
            int adjacency3 = adjTriangles[indexOfLastTriangleCreated][2];
            // if any of these are -1, meaning there is no adjacency on that side, fine
            // that won't have changed, but it might have otherwise
            if (adjacency1 >= 0) {
                for (int m = 0; m < 3; m++) {
                    if (adjTriangles[adjacency1][m] == indexOfLastTriangleCreated) {
                        adjTriangles[adjacency1][m] = indexOfLastTriangleCreated; // redundant, here for clarity
                        break;
                    }
                }
            }
            if (adjacency2 >= 0) {
                for (int m = 0; m < 3; m++) {
                    if (adjTriangles[adjacency2][m] == indexOfLastTriangleCreated) {
                        adjTriangles[adjacency2][m] = numTriangles - 2;
                        break;
                    }
                }
            }
            if (adjacency3 >= 0) {
                for (int m = 0; m < 3; m++) {
                    if (adjTriangles[adjacency3][m] == indexOfLastTriangleCreated) {
                        adjTriangles[adjacency3][m] = numTriangles - 1;
                        break;
                    }
                }
            }
            // next, set up adjacencies of the newly made triangles
            adjTriangles[numTriangles - 2][0] = indexOfLastTriangleCreated;
            adjTriangles[numTriangles - 2][1] = adjTriangles[indexOfLastTriangleCreated][1];
            adjTriangles[numTriangles - 2][2] = numTriangles - 1;
            adjTriangles[numTriangles - 1][0] = numTriangles - 2;
            adjTriangles[numTriangles - 1][1] = adjTriangles[indexOfLastTriangleCreated][2];
            adjTriangles[numTriangles - 1][2] = indexOfLastTriangleCreated;
            // now we update the old first triangle, it's no longer needed
            // vertices
            verticesOfTriangles[indexOfLastTriangleCreated][2] = verticesOfTriangles[indexOfLastTriangleCreated][1];
            verticesOfTriangles[indexOfLastTriangleCreated][1] = verticesOfTriangles[indexOfLastTriangleCreated][0];
            verticesOfTriangles[indexOfLastTriangleCreated][0] = pointIndex;
            // adjacencies
            adjTriangles[indexOfLastTriangleCreated][1] = adjTriangles[indexOfLastTriangleCreated][0];
            adjTriangles[indexOfLastTriangleCreated][2] = numTriangles - 2;
            adjTriangles[indexOfLastTriangleCreated][0] = numTriangles - 1;
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
                double sinA = ((vertex1.x - vertex3.x) * (vertex2.y - vertex3.y) - (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x));
                double sinB = ((vertex2.x - P.x) * (vertex1.y - P.y) - (vertex2.y - P.y) * (vertex1.x - P.x));

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
                            verticesOfTriangles[triangleR][0] = verticesOfTriangles[triangleR][1];
                            verticesOfTriangles[triangleR][1] = verticesOfTriangles[triangleR][2];
                            verticesOfTriangles[triangleR][2] = temp1;
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
            }

        }

        // step 9: kill anything related to the supertriangle
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
        int numTrianglesFinal = numTriangles-numDeadTriangles;

        int[][] finalVerticesOfTriangles = new int[numTrianglesFinal * 3][3];
        int index = 0;
        for (int i = 0; i < numTriangles; i++) {
            if (!(deadTriangles[i] == 1)) {
                finalVerticesOfTriangles[index][0] = verticesOfTriangles[i][0];
                finalVerticesOfTriangles[index][1] = verticesOfTriangles[i][1];
                finalVerticesOfTriangles[index++][2] = verticesOfTriangles[i][2];
            }
        }

        // step 10: reverse the mapping of points to the area near (0,0)
        numPoints -= 3;
        deNormalizePoints();

        // algorithm completed, translate into a usable output and return
        return parseTriangulation(finalVerticesOfTriangles);
    }

    private ArrayList<Connection> parseTriangulation(int[][] trianglePoints) {

        ArrayList<Connection> connections = new ArrayList<>();

        for (int[] triangle : trianglePoints) {

            MyPoint2D point1 = points[triangle[0]];
            MyPoint2D point2 = points[triangle[1]];
            MyPoint2D point3 = points[triangle[2]];

            connections.add(new Connection(point1,point2));
            connections.add(new Connection(point2,point3));
            connections.add(new Connection(point3,point1));

        }

        ArrayList<Connection> seen = new ArrayList<>();

        for (Connection con : connections) {
            if (!(seen.contains(con))) {
                seen.add(con);
            }
        }

        return seen;

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
                double crossProduct = 0;
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
        for (int i = 0; i < points.length-3; i++) {
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

