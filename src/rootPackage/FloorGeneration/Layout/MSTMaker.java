package rootPackage.FloorGeneration.Layout;

import rootPackage.FloatEquivalence;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * A class to generate a Minimum-Spanning Tree (MST) from the mesh returned by a Delaunay Triangulation of a point cloud.
 * An MST is a tree structure that traverses a graph of weighted edges with the lowest possible total cost.
 * In more detail, you take a graph like the one made by a Delaunay Triangulation and choose a series of lines that touch every point in the graph at least once.
 * The lengths of these lines are their weights, in this case.
 * An MST of such a graph would be one where the combined weights of every line is as low as possible.
 *
 * @author William Owens
 * @version 1.0
 */
public class MSTMaker {

    private int numNodes;
    private AdjacencyMatrix graph;

    public static void main(String[] args) {
        // seeded demonstration of level gen (10 points between (1,1) and (10,10), seed 44)
        Random rng = new Random(44);
        Point2D.Double[] inputs = new Point2D.Double[10];
        for (int i = 0; i < 10; i++) {
            Point2D.Double point = new Point2D.Double(rng.nextInt(10) + 1, rng.nextInt(10) + 1);
            boolean exist = false;
            for (Point2D.Double pointInArray : inputs) {
                if (point.equals(pointInArray)) {
                    exist = true;
                }
            }
            if (!exist) {
                inputs[i] = point;
                continue;
            }
            i--;

        }
        DelaunayTriangulation del = new DelaunayTriangulation(inputs);
        Triangulation triangulation = del.triangulate(); // triangulate the inputs
        MSTMaker mst = new MSTMaker(triangulation);
        MinimumSpanningTree minimumSpanningTree = mst.generateMST();

    }

    /**
     * Constructor. Sets up this MSTMaker with a given triangulation.
     * @param delaunayTriangulation The output of a DelaunayTriangulation's .triangulate() method.
     */
    public MSTMaker(Triangulation delaunayTriangulation) {
        ArrayList<MyPoint2D> pointsOfTriangulation = delaunayTriangulation.getPoints();
        ArrayList<Connection> connectionssOfTriangulation = delaunayTriangulation.getConnections();

        // set up num nodes and the empty adjacency matrix
        this.numNodes = pointsOfTriangulation.size();
        MyPoint2D[] nodes = new MyPoint2D[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodes[i] = (MyPoint2D) pointsOfTriangulation.get(i);
        }
        this.graph = new AdjacencyMatrix(nodes);

        // propagate the adjacency matrix
        Connection[] connections = new Connection[connectionssOfTriangulation.size()];
        for (int i = 0; i < connections.length; i++) {
            connections[i] = (Connection) connectionssOfTriangulation.get(i);
        }
        for (Connection connection : connections) {
            graph.addConnection(connection.getOrigin(), connection.getDestination(), connection.getWeight());
        }
    }

    /**
     * Getter for this MSTMaker's adjacency matrix.
     */
    public AdjacencyMatrix getGraph() {
        return graph;
    }

    /**
     * Generates an MST for the supplied graph using Prim's algorithm.
     * @return An arraylist of Connections that, together, form the MST.
     *
     * @see <a href="https://youtu.be/jsmMtJpPnhU">Video on MSTs and Prim's Algorithm by YouTube user WilliamFiset</a>
     */
    public MinimumSpanningTree generateMST() {
        ArrayList<MSTEdge> priorityQueue = new ArrayList<>();
        ArrayList<MSTEdge> minimumSpanningTree = new ArrayList<>();
        boolean[] visited = new boolean[numNodes];
        int numEdgesInMST = numNodes-1;
        int currentNode = 0;
        // while the MST isn't finished, and the priorityqueue isn't empty, do the logic
        do {
            // visit the current node, add its edges to the PQ (if they aren't already there)
            visited[currentNode] = true;
            double[] connections = graph.getNodeConnectionIndexes(currentNode);
            for (int i = 0; i < connections.length; i++) {
                // if i is visited, skip this one
                // if the connection does not point to anything, skip this one
                if (visited[i] || connections[i] == 0) {
                    continue;
                }
                priorityQueue.add(new MSTEdge(currentNode,i,connections[i]));
            }
            // if new edges have been added, logic can be done normally
            // if no new edges get added, more must be done
            while (visited[currentNode]) {
                // MSTs sort by lowest weight first
                Collections.sort(priorityQueue);
                MSTEdge bestEdge = priorityQueue.get(0);
                priorityQueue.remove(0);
                if (visited[bestEdge.getEnd()]) {
                    // if the best edge in the PQ does not point to a new node, ignore that edge
                    // we do this to avoid cycles, which would result in a) not a real MST and b) an infinite loop
                    continue;
                }
                // best edge points to a new node, add it
                minimumSpanningTree.add(bestEdge);
                currentNode = bestEdge.getEnd();
            }
        } while (minimumSpanningTree.size() < numEdgesInMST && priorityQueue.size() > 0);

        ArrayList<Connection> output = new ArrayList<>();
        for (MSTEdge ed : minimumSpanningTree) {
            FloorLayoutGenerator.MSTSb.append(ed.toString());
            FloorLayoutGenerator.MSTSb.append("\n");
            MyPoint2D origin = graph.getNode(ed.getStart());
            MyPoint2D destination = graph.getNode(ed.getEnd());
            output.add(new Connection(origin,destination));
        }

        MinimumSpanningTree MST = new MinimumSpanningTree(output.toArray(new Connection[output.size()]), minimumSpanningTree.toArray(new MSTEdge[minimumSpanningTree.size()]), graph);

        return MST;
    }
}

/**
 * Class representing a connection between nodes of an adjacency matrix.
 * Very similar to a Connection, but stores data in a form more convenient for this class' logic.
 *
 * @author William Owens
 * @version 1.0
 */
class MSTEdge implements Comparable<MSTEdge>{

    private int start;
    private int end;
    private double weight;

    public MSTEdge(int start, int end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(MSTEdge o) {
        if (FloatEquivalence.equals(o.weight,this.weight)) {
            return 0;
        }
        else if (o.weight>this.weight) {
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "\\operatorname{polygon}\\left(p_{%d},p_{%d}\\right)".formatted(start,end);
    }
}
