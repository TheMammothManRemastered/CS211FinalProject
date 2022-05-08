package rootPackage;

import rootPackage.Battle.BattleSupervisor;
import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Graphics.MainWindow;
import rootPackage.Level.Floor;
import rootPackage.Level.FloorGeneration.FloorGenerator;
import rootPackage.Level.FloorGeneration.FloorTheme;
import rootPackage.Level.FloorGeneration.FloorThemeGenerator;

import org.json.simple.*;

import javax.swing.*;

/**
 * The application entry point.
 * Provides a point from which various systems can access other systems they may not otherwise have a connection to.
 *
 * @author William Owens
 * @version 3.2
 */
public class Main extends JPanel {

    public static MainWindow mainWindow; // main game window
    public static Player player; // player object reference
    public static Floor currentFloor; // reference to the current floor
    public static boolean gameOver = false; // if this is true, the parser will stop accepting inputs
    public static BattleSupervisor currentBattle = null;
    public static BattleManagerMain battleManagerMain; // thread responsible for managing battle logic and player input concurrently
    public static long timeStart; // used in calculating playtime
    private static int difficulty = 1;
    private static Floor shopFloor; // the same shop is used every time a shop is needed, this prevents exploits

    public static void main(String[] args) {
        timeStart = System.nanoTime();
        setup();
        battleManagerMain = new BattleManagerMain();
    }

    /**
     * Responsible for setting up the various fields in Main.
     */
    private static void setup() {
        mainWindow = new MainWindow();
        FloorTheme theme = FloorThemeGenerator.generateFloorTheme(difficulty++);
        currentFloor = FloorGenerator.generateFloor(theme);
        shopFloor = FloorGenerator.generateShop();
        player = new Player(currentFloor.getSpawn());
        mainWindow.getConsoleWindow().addEntryToHistory("Welcome to the dungeon. You have been tasked by the gods of Olympus to journey deep into the dungeon and defeat the Titan Lord of Time, Chronos. Good luck, adventurer.");
        mainWindow.getConsoleWindow().addEntryToHistory(ConsoleWindow.TUTORIAL);
        mainWindow.getConsoleWindow().addEntryToHistory("(To see this tutorial again, type \"tutorial\" at any time.)");
        mainWindow.getConsoleWindow().addEntryToHistory("(And here's a quick hint, you should always examine rooms when you go into them. There could always be something you don't see right away...)");
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature()); //TODO: refactor this into a single method in ViewportWindow
    }

    /**
     * Sets the current floor to the shop floor and moves the player to it.
     */
    public static void moveToShop() {
        currentFloor = shopFloor;
        player.setCurrentRoom(currentFloor.getSpawn());
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature());
    }

    /**
     * Generates a new floor of the next highest difficulty and moves the player to it.
     */
    public static void moveToNewFloor() {
        currentFloor = new FloorGenerator().generateFloor(FloorThemeGenerator.generateFloorTheme(difficulty++));
        player.setCurrentRoom(currentFloor.getSpawn());
        mainWindow.getViewportPanel().drawRoom(player.getCurrentRoom().getRoomAsFeature());
    }

}

