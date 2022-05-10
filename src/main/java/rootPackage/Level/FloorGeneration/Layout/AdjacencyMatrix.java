package rootPackage.Level.FloorGeneration.Layout;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing an Adjacency Matrix, a data structure that holds information on the adjacencies of an array of nodes with weighted edges.
 *
 * @author William Owens
 * @version 1.0
 */
public class AdjacencyMatrix {

    private final double[][] matrix;
    private final MyPoint2D[] nodes;

    /**
     * Constructor. Creates an empty adjacency matrix of the correct size for the number of nodes being used.
     * <p>
     * NOTE: This does NOT propagate the adjacencies of the nodes, this must be done elsewhere via the addConnection() method.
     *
     * @see <a href="https://mathworld.wolfram.com/AdjacencyMatrix.html">Explanation of an adjacency matrix at wolfram.com. </a> This implementation differs slightly, no adjacency is still denoted by a 0, but an adjacency being present is represented by its weight, not just a 1.
     * @see AdjacencyMatrix#addConnection(MyPoint2D, MyPoint2D, double) addConnection()
     */
    public AdjacencyMatrix(MyPoint2D... nodes) {
        int numNodes = nodes.length;
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

    public int size() {
        return matrix.length;
    }

    /**
     * Returns an array of connections as a row of the adjacency matrix
     */
    public double[] getNodeConnectionIndexes(int index) {
        return matrix[index];
    }

    /**
     * Returns an array of connections
     */
    public Connection[] getNodeConnections(int index) {
        Connection[] output = new Connection[getCountOfNodeConnections(index)];
        int outputIndex = 0;
        for (int i = 0; i < this.size(); i++) {
            if (matrix[index][i] > 0) {
                output[outputIndex] = new Connection(getNode(index), getNode(i));
                outputIndex++;
            }
        }
        return output;
    }

    public int getCountOfNodeConnections(int index) {
        int count = 0;
        for (double d : matrix[index]) {
            if (d > 0) count++;
        }
        return count;
    }

    /**
     * Adds a connection and its weight to the matrix.
     *
     * @param source      Index of the start node in the matrix.
     * @param destination Index of the end node in the matrix.
     */
    public void addConnection(int source, int destination, double weight) {
        matrix[source][destination] = weight;
    }

    /**
     * Adds a connection and its weight to the matrix.
     *
     * @param source      Start node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     * @param destination End node (must be in the matrix or an IndexOutOfBoundsException will be raised).
     */
    public void addConnection(MyPoint2D source, MyPoint2D destination, double weight) throws IndexOutOfBoundsException {
        List<MyPoint2D> nodesList = Arrays.asList(nodes);
        int sourceIndex = nodesList.indexOf(source);
        int destinationIndex = nodesList.indexOf(destination);
        matrix[sourceIndex][destinationIndex] = weight;
    }
}
