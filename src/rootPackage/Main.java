package rootPackage;

import rootPackage.FloorGeneration.Features.Feature;
import rootPackage.FloorGeneration.Floor;
import rootPackage.FloorGeneration.Layout.RoomNode;
import rootPackage.Graphics.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class will, eventually, be the 'starter' class that launches the whole game.
 * Currently, it is responsible for running the floor generation demo.
 *
 * @author William Owens
 * @version 0.2
 */
public class Main extends JPanel {

    public static final int PIXELS_PER_CARTESIAN_POINT = 60;
    public static MainWindow mainWindow;
    private static Floor floor;

    private BufferedImage compass;
    private RoomNode spawnLocation;
    private Player player;

    public static void main(String[] args) {

        mainWindow = new MainWindow();
    }

    /**
     * Override of the paint method, is responsible for telling the graphics in the window what to draw and where.
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // convert to graphics2d because that can draw shapes, lines specifically
        Graphics2D g2d = (Graphics2D) g;

        // set color sets the color for drawing things to the screen, but it isn't retroactive.
        // fill the screen with black
        g2d.setColor(Color.BLACK);
        Rectangle2D.Double fill = new Rectangle2D.Double(0, 0, 854, 480);
        g2d.fill(fill);
    }

}
