package rootPackage.FloorGeneration.Layout;

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

    public void setPoints(ArrayList<MyPoint2D> points) {
        this.points = points;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    private void removeDuplicates() {
        for (MyPoint2D point : points) {
            System.out.println(point);
        }
        System.out.println();
        for (Connection point : connections) {
            System.out.println(point);
        }
        System.out.println("\n\n\n\n");
        points = removeDuplicatesFromList(points);
        connections = removeDuplicatesFromList(connections);
        for (MyPoint2D point : points) {
            System.out.println(point);
        }
        System.out.println();
        for (Connection point : connections) {
            System.out.println(point);
        }
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
