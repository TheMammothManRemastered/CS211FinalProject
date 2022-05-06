package rootPackage;

import rootPackage.Battle.BattleSupervisor;
import rootPackage.Level.Floor;
import rootPackage.Level.FloorGeneration.FloorGenerator;
import rootPackage.Level.FloorGeneration.FloorTheme;
import rootPackage.Level.FloorGeneration.FloorThemeGenerator;
import rootPackage.Graphics.MainWindow;
import rootPackage.Input.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

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
    public static Floor currentFloor;
    public static boolean gameOver = false;
    private static int difficulty = 1;
    public static BattleSupervisor currentBattle = null;

    public static BattleManagerMain battleManagerMain;

    public static long timeStart;

    public synchronized static void main(String[] args) {
        timeStart = System.nanoTime();
        setup();
        battleManagerMain = new BattleManagerMain();
    }

    private static void setup() {
        mainWindow = new MainWindow();
        FloorTheme theme = FloorThemeGenerator.generateFloorTheme(difficulty++);
        currentFloor = new FloorGenerator().generateFloor(theme);
        player = new Player(currentFloor.getSpawn());
        System.out.println("set up?");
        mainWindow.getConsoleWindow().addEntryToHistory("Welcome to the dungeon. You have been tasked by the gods of Olympus to journey deep into the dungeon and defeat the Titan Lord of Time, Chronos. Good luck, adventurer.");
        mainWindow.getConsoleWindow().addEntryToHistory("(To play this game, simply type what you want to do into the input box below, and press the ENTER key. For instance, if you were in a room with a door to the north, you might try \"Move to the north\" or \"Travel north\".)");
        mainWindow.getConsoleWindow().addEntryToHistory("(To see this tutorial again, type \"tutorial\" at any time.)");
        mainWindow.getConsoleWindow().addEntryToHistory("(And here's a quick hint, you should always examine rooms when you go into them. There could always be something you don't see right away...)");
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature());
    }

    public Player getPlayer() {
        return player;
    }

    public static void moveToShop() {
        currentFloor = FloorGenerator.generateShop();
        player.setCurrentRoom(currentFloor.getSpawn());
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature());
    }

    public static void moveToNewFloor() {
        currentFloor = new FloorGenerator().generateFloor(FloorThemeGenerator.generateFloorTheme(difficulty++));
        player.setCurrentRoom(currentFloor.getSpawn());
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature());
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

