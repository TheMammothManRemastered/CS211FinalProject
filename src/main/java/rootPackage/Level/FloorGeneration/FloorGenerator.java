package rootPackage.Level.FloorGeneration;

import rootPackage.Level.FloorGeneration.Layout.*;
import rootPackage.Level.Floor;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * The floor generator class is responsible for generating the Floor objects for each level.
 *
 * @author William Owens
 * @version 2.0
 */
public class FloorGenerator {

    private RoomNode[] layout;
    private Floor floor;

    public FloorGenerator() {
        layout = null;
    }

    public static void main(String[] args) {
        FloorGenerator flg = new FloorGenerator();
        flg.generateFloor(new FloorTheme(15,20,2,4,null,null,null,null,null));
    }

    //TODO: make generateFloor take a parameter defining the things the floor has to have (ie. at least 2 dead ends, at least x features, all that. basically the floor's theme)

    /**
     * Generates the floor. If there is no layout set already (which will usually be the case) one will be generated here.
     */
    public Floor generateFloor(FloorTheme theme) {

        // generate the layout
        if (this.layout == null) {
            // first, generate a valid MST with no sharp connections
            boolean validMST = false;
            MinimumSpanningTree minimumSpanningTree = null;
            Triangulation triangulation = null;
            while (!validMST) {
                //TODO: this is currently very seeded and hardcoded
                int sizeOfFloor = theme.getMinimumSize() + FloorGenerationRNG.rng.nextInt(theme.getMaximumSize() - theme.getMinimumSize()) + 1;
                Point2D.Double[] inputs = new Point2D.Double[sizeOfFloor];
                for (int i = 0; i < sizeOfFloor; i++) {
                    Point2D.Double point = new Point2D.Double(FloorGenerationRNG.rng.nextInt(50) + 1, FloorGenerationRNG.rng.nextInt(50) + 1);
                    boolean exist = false;
                    for (Point2D.Double pointInArray : inputs) {
                        if (point.equals(pointInArray)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        inputs[i] = point;
                        continue;
                    }
                    i--;

                }

                DelaunayTriangulation del = new DelaunayTriangulation(inputs);
                triangulation = del.triangulate(); // triangulate the inputs

                MSTMaker mst = new MSTMaker(triangulation);
                minimumSpanningTree = mst.generateMST();

                validMST = (minimumSpanningTree.getDeadEnds().length >= theme.getDeadEndsNeeded()) && minimumSpanningTree.isMSTValid();
            }

            // now, add random connections from the delaunay triangulation to anywhere other than dead ends
            MyPoint2D[] deadEnds = minimumSpanningTree.getDeadEnds();
            ArrayList<Connection> connectionsInDelaunayTriangulation = triangulation.getConnections();
            ArrayList<Connection> connectionsNotAttachedToDeadEnds = new ArrayList<>();
            // get all the connections from the original triangulation that are not connected to any dead end
            for (Connection connection : connectionsInDelaunayTriangulation) {
                if (!connection.containsAny(deadEnds)) {
                    connectionsNotAttachedToDeadEnds.add(connection);
                }
            }
            // remove elements already contained in the minimum spanning tree
            for (Connection connection : minimumSpanningTree.getConnections()) {
                connectionsNotAttachedToDeadEnds.remove(connection);
                connectionsNotAttachedToDeadEnds.remove(connection.opposite());
            }

            // delete duplicate elements
            for (int i = 0; i < connectionsNotAttachedToDeadEnds.size(); i++) {
                Connection connection = connectionsNotAttachedToDeadEnds.get(i);
                connectionsNotAttachedToDeadEnds.remove(connection.opposite());
            }

            //TODO: this. mst generation is fine as is, this is sorta just quality of life
            // although, snakey floor layouts are REALLY common

            FloorLayoutGenerator flg = new FloorLayoutGenerator(minimumSpanningTree);
            this.layout = flg.generateFloor();
            this.floor = new Floor(new ArrayList<>(Arrays.asList(layout)));
        }


        for (RoomNode r : layout) {
            r.printRoomAndConnections();
        }

        return floor;
    }


}
