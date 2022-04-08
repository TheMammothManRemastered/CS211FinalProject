package rootPackage.FloorGeneration.Layout;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FloorLayoutGenerator {

    public static StringBuilder delaunayStagesSb = new StringBuilder();
    public static StringBuilder MSTSb = new StringBuilder();

    /**
     * This class' main method exists for demonstrative purposes only, and should not be called under normal operation.
     * @param args
     */
    public static void main(String[] args) {
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
        ArrayList[] triangulation = del.triangulate();
        StringBuilder sb = new StringBuilder();
        sb.append("Input points (numbered)\n");
        for (int i = 0; i < triangulation[0].size(); i++) {
            sb.append(("p_{%d}=").formatted(i));
            sb.append(triangulation[0].get(i).toString());
            sb.append("\n");
        }
        sb.append("\nDelaunay Triangulation\n");
        sb.append(delaunayStagesSb.toString());
        sb.append("\nMinimum Spanning Tree generation\n");
        MSTMaker mst = new MSTMaker(triangulation);
        ArrayList<Connection> minimumSpanningTree = mst.generateMST();
        sb.append(MSTSb);

        System.out.println(sb);

    }

}
