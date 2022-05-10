package rootPackage.Level.FloorGeneration.Layout;

import rootPackage.Direction;

import java.util.ArrayList;

/**
 * A class representing an MST.
 *
 * @author William Owens
 * @version 1.5
 */
public class MinimumSpanningTree {

    private final Connection[] connections;
    private final MyPoint2D[] points;
    private final AdjacencyMatrix mstGraph;

    public MinimumSpanningTree(Connection[] connections, MSTEdge[] edges, AdjacencyMatrix rawGraph) {
        this.connections = connections;
        points = new MyPoint2D[rawGraph.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = rawGraph.getNode(i);
        }
        this.mstGraph = new AdjacencyMatrix(points);
        for (MSTEdge edge : edges) {
            mstGraph.addConnection(edge.getStart(), edge.getEnd(), edge.getWeight());
            mstGraph.addConnection(edge.getEnd(), edge.getStart(), edge.getWeight());
        }

    }

    public Connection[] getConnections() {
        return connections;
    }

    public AdjacencyMatrix getMstGraph() {
        return mstGraph;
    }

    public int size() {
        return mstGraph.size();
    }

    public MyPoint2D[] getDeadEnds() {
        ArrayList<MyPoint2D> outputList = new ArrayList<>();
        for (int i = 0; i < mstGraph.size(); i++) {
            if (mstGraph.getCountOfNodeConnections(i) == 1) {
                outputList.add(mstGraph.getNode(i));
            }
        }
        return outputList.toArray(new MyPoint2D[0]);
    }

    public MyPoint2D getPointByIndex(int index) {
        return points[index];
    }

    /**
     * Checks if the MST is 'valid'.
     * An MST is valid if:
     * no single point has more than 4 connections
     * none of the connections coming off a single node are too close to one another
     */
    public boolean isMSTValid() {
        // check, for each point, if connections are valid
        int numPoints = mstGraph.size();
        for (int i = 0; i < numPoints; i++) {
            // does this point have more than 4 connections?
            int numConnectionsFromCurrentNode = mstGraph.getCountOfNodeConnections(i);
            if (numConnectionsFromCurrentNode > 4) {
                return false;
            }

            // are the connections from this point too close to one another?
            Connection[] connectionsFromPoint = mstGraph.getNodeConnections(i);
            for (int j = 0; j < connectionsFromPoint.length; j++) {
                Connection connectionBeingChecked = connectionsFromPoint[j];
                double degreesOfConnection = connectionBeingChecked.getRelativeDegrees();
                Direction directionOfConnection = Direction.degreesToDirection(degreesOfConnection);

                for (int k = 0; k < connectionsFromPoint.length; k++) {
                    if (k == j) {
                        continue;
                    }
                    if (directionOfConnection.equals(Direction.degreesToDirection(connectionsFromPoint[k].getRelativeDegrees()))) {
                        return false;
                    }

                }
            }
        }

        return true;
    }


}
