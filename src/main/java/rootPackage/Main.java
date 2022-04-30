package rootPackage;

import rootPackage.Level.Features.Equipment.ArmorFeature;
import rootPackage.Level.Features.TopLevel.PlayerFeature;
import rootPackage.Level.Features.TopLevel.Room;
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
        mainWindow = new MainWindow();
        RoomNode phRoomNode = new RoomNode();
        phRoomNode.setRoomAsFeature(new Room("Room", new String[]{"man", "ass"}));
        phRoomNode.getRoomAsFeature().addChild(new ArmorFeature("Lion Armor", new String[]{"armor"}));
        player = new Player(phRoomNode);
        player.setPlayerAsFeature(new PlayerFeature("You", new String[]{"player", "self", "you"}));
        player.getPlayerAsFeature().updateCurrentRoom(player);
        player.getPlayerAsFeature().addChild(new ArmorFeature("Diamond Armor", new String[]{"armor"}));
        mainWindow.getConsoleWindow().addEntryToHistory("You are Halo 3, an accurate and thrifty marksman from McDonalds. You are facing the notorious Man Behind The Slaughter, a horrid criminal from Freddy Fazbear's Pizza, a magical place for kids and grown-ups alike. What do you do?");
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Override of the paint method, is responsible for telling the graphics in the window what to draw and where.
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
