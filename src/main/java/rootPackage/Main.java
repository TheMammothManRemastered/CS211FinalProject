package rootPackage;

import rootPackage.Level.Floor;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;
import rootPackage.Graphics.MainWindow;
import rootPackage.Input.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
    public static Player player;
    public static Parser parser;

    private Floor floor;
    private BufferedImage compass;
    private RoomNode spawnLocation;

    public static void main(String[] args) {
        parser = new Parser();
        mainWindow = new MainWindow();
        player = new Player(null);
        mainWindow.getConsoleWindow().addEntryToHistory("You are Halo 3, an accurate and thrifty marksman from McDonalds. You are facing the notorious Man Behind The Slaughter, a horrid criminal from Freddy Fazbear's Pizza, a magical place for kids and grown-ups alike. What do you do?");
    }

    public Player getPlayer() {
        return player;
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
