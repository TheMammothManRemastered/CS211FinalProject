package rootPackage.FloorGeneration.Layout;

import rootPackage.Direction;
import rootPackage.FloatEquivalence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private AdjacencyMatrix mstGraph;

    public MinimumSpanningTree(Connection[] connections, MSTEdge[] edges, AdjacencyMatrix rawGraph) {
        this.connections = connections;
        this.edges = edges;
        points = new MyPoint2D[rawGraph.getSize()];
        for (int i = 0; i < points.length; i++) {
            points[i] = rawGraph.getNode(i);
        }
        this.mstGraph = new AdjacencyMatrix(points);
        MSTEdge[] mstEdges = this.edges;
        for (int i = 0; i < mstEdges.length; i++) {
            MSTEdge edge = mstEdges[i];
            mstGraph.addConnection(edge.getStart(), edge.getEnd(), edge.getWeight());
            mstGraph.addConnection(edge.getEnd(), edge.getStart(), edge.getWeight());
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
        double[] connectionsArray = mstGraph.getNodeConnectionIndexes(index);

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

    public boolean isMSTValid() {
        // check, for each point, if connections are valid
        int numPoints = mstGraph.getSize();
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

    public Connection[] getConnections() {
        return connections;
    }

    public boolean containsConnection(Connection con) {
        return List.of(connections).contains(con);
    }

    public void addConnectionIfValid(Connection connectionToAdd) {
        Connection[] connectionsBackup = new Connection[this.connections.length];

        Connection[] connections = new Connection[this.connections.length+1];
        connections[connections.length-1] = connectionToAdd;
        for (int i = 0; i < this.connections.length; i++) {
            connections[i] = this.connections[i];
            connectionsBackup[i] = this.connections[i];
        }

        mstGraph.addConnection(connectionToAdd.getOrigin(), connectionToAdd.getDestination(), connectionToAdd.getWeight());
        mstGraph.addConnection(connectionToAdd.getDestination(), connectionToAdd.getOrigin(), connectionToAdd.getWeight());

        if (!isMSTValid()) {
            this.connections = connectionsBackup;
            mstGraph.removeConnectionBothWays(connectionToAdd);
        }


    }

    public int getSize() {
        return mstGraph.getSize();
    }

    public AdjacencyMatrix getMstGraph() {
        return mstGraph;
    }

    public void printMST() {
        for (Connection con : connections) {
            System.out.println(con);
        }
    }


}
