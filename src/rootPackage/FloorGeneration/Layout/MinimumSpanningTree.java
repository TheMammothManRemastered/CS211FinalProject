package rootPackage.FloorGeneration.Layout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing an MST.
 *
 * @author William Owens
 * @version 1.0
 */
public class MinimumSpanningTree {

    private Connection[] connections;
    private MSTEdge[] edges;
    private MyPoint2D[] points;
    private AdjacencyMatrix rawGraph;
    private AdjacencyMatrix mstGraph;

    public MinimumSpanningTree(Connection[] connections, MSTEdge[] edges, AdjacencyMatrix rawGraph) {
        this.connections = connections;
        this.edges = edges;
        this.rawGraph = rawGraph;
        points = new MyPoint2D[rawGraph.getSize()];
        for (int i = 0; i < points.length; i++) {
            points[i] = rawGraph.getNode(i);
        }
        this.mstGraph = new AdjacencyMatrix(points);
        MSTEdge[] mstEdges = this.edges;
        for (int i = 0; i < mstEdges.length; i++) {
            MSTEdge edge = mstEdges[i];
            mstGraph.addConnection(edge.getStart(),edge.getEnd(),edge.getWeight());
            mstGraph.addConnection(edge.getEnd(),edge.getStart(),edge.getWeight());
        }

    }

    public MyPoint2D[] getDeadEnds() {
        ArrayList<MyPoint2D> outputList = new ArrayList<>();
        for (int i = 0; i < mstGraph.getSize(); i++) {
            if (mstGraph.getCountOfNodeConnections(i) == 1) {
                outputList.add(mstGraph.getNode(i));
            }
        }
        return outputList.toArray(new MyPoint2D[outputList.size()]);
    }

    public MyPoint2D[] getConnectedPoints(MyPoint2D origin) {
        ArrayList<MyPoint2D> outputList = new ArrayList<>();
        int index = mstGraph.getIndexOfNode(origin);
        double[] connectionsArray = mstGraph.getNodeConnections(index);

        for (int i = 0; i < connectionsArray.length; i++) {
            if (connectionsArray[i] > 0) {
                outputList.add(mstGraph.getNode(i));
            }
        }
        return outputList.toArray(new MyPoint2D[outputList.size()]);
    }

    public MyPoint2D getPointByIndex(int index) {
        return points[index];
    }

    public void printMST() {
        for (Connection con : connections) {
            System.out.println(con);
        }
    }



}
