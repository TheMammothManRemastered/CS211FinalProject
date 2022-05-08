package rootPackage.Level.FloorGeneration.Layout;

import org.json.simple.*;
import java.util.ArrayList;

/**
 * The output of a delaunay triangulation.
 * Contains a list of the points in the triangulation, and a list of its edges.
 */
public class Triangulation {

    private ArrayList<MyPoint2D> points;
    private ArrayList<Connection> connections;

    public Triangulation(ArrayList<MyPoint2D> points, ArrayList<Connection> connections) {
        this.points = points;
        this.connections = connections;
        removeDuplicates();
    }

    public ArrayList<MyPoint2D> getPoints() {
        return points;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setPoints(ArrayList<MyPoint2D> points) {
        this.points = points;
    }

    private void removeDuplicates() {
        points = removeDuplicatesFromList(points);
        connections = removeDuplicatesFromList(connections);
    }

    private <T> ArrayList<T> removeDuplicatesFromList(ArrayList<T> input) {
        ArrayList<T> seen = new ArrayList<>();
        for (T t : input) {
            if (!(seen.contains(t))) {
                seen.add(t);
            }
        }
        return seen;
    }
}
