package rootPackage.FloorGeneration.Layout;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing an Adjacency Matrix, a data structure that holds information on the adjacencies of an array of nodes with weighted edges.
 *
 * @author William Owens
 * @version 1.0
 */
public class AdjacencyMatrix {

    private int numNodes;
    private double[][] matrix;
    private MyPoint2D[] nodes;

    /**
     * Constructor. Creates an empty adjacency matrix of the correct size for the number of nodes being used.
     *
     * NOTE: This does NOT propagate the adjacencies of the nodes, this must be done externally via the addConnection() method.
     *
     * @see <a href="https://mathworld.wolfram.com/AdjacencyMatrix.html">Explanation of an adjacency matrix at wolfram.com. </a> This implementation differs slightly, no adjacency is still denoted by a 0, but an adjacency being present is represented by its weight, not just a 1.
     * @see AdjacencyMatrix#addConnection(MyPoint2D, MyPoint2D, double) addConnection()
     */
    public AdjacencyMatrix(MyPoint2D... nodes) {
        numNodes = nodes.length;
        this.nodes = Arrays.copyOf(nodes, numNodes);
        matrix = new double[numNodes][numNodes];
    }

    /**
     * Returns a MyPoint2D node by its index in the matrix.
     */
    public MyPoint2D getNode(int index) {
        return nodes[index];
    }

    /**
     * Returns the index of a node in the matrix, or -1 if it is not present.
     */
    public int getIndexOfNode(MyPoint2D node) {
        List<MyPoint2D> nodesList = Arrays.asList(nodes);
        return nodesList.indexOf(node);
    }

    /**
     * Returns an array of connections
     * @param index
     * @return
     */
    public double[] getNodeConnections(int index) {
        return matrix[index];
    }

    /**
     * Adds a connection and its weight to the matrix.
     * @param source Index of the start node in the matrix.
     * @param destination Index of the end node in the matrix.
     */
    public void addConnection(int source, int destination, double weight) {
        matrix[source][destination] = weight;
    }

    /**
     * Gets the weight of a connection.
     * @param source Index of the start node in the matrix.
     * @param destination Index of the end node in the matrix.
     */
    public double getConnectionWeight(int source, int destination) {
        return matrix[source][destination];
    }

    // following 2 methods are overloads meant to use references to point objects rather than their indices
    /**
     * Adds a connection and its weight to the matrix.
     * @param source Start node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     * @param destination End node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     */
    public void addConnection(MyPoint2D source, MyPoint2D destination, double weight) throws IndexOutOfBoundsException {
        List<MyPoint2D> nodesList = Arrays.asList(nodes);
        int sourceIndex = nodesList.indexOf(source);
        int destinationIndex = nodesList.indexOf(destination);
        matrix[sourceIndex][destinationIndex] = weight;
    }

    /**
     * Gets the weight of a connection.
     * @param source Start node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     * @param destination End node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     */
    public double getConnectionWeight(MyPoint2D source, MyPoint2D destination) {
        List<MyPoint2D> nodesList = Arrays.asList(nodes);
        int sourceIndex = nodesList.indexOf(source);
        int destinationIndex = nodesList.indexOf(destination);
        return matrix[sourceIndex][destinationIndex];
    }

    public void printMatrix() {
        for (double[] line : matrix) {
            for (double d : line) {
                System.out.printf("%9f ", d);
            }
            System.out.println();
        }
    }
}
