package rootPackage.Graphics.GUI;

import rootPackage.Input.Parser;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Equipment.*;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Logic.GameManager;
import rootPackage.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * The ConsoleWindow class is a JPanel container holding the 'console' system.
 * This class is responsible for taking user input, showing the user's input history, and showing the user the result of their actions.
 *
 * @author William Owens
 * @version 2.2
 */
public class ConsoleWindow extends JPanel {

    public static final String TUTORIAL = """
            (To play this game, simply type what you want to do into the input box below, and press the ENTER key. For instance, if you were in a room with a door to the north, you might try "Move to the north" or "Travel north".)(
            There are also a few special commands that do not follow that verb-noun structure.)
            ("Tutorial" brings up this dialogue, in case you need a refresher.)
            ("Map" brings up a map of the floor.)
            ("Status" displays some information about yourself.)""";

    private final int WIDTH = 1280;
    private final int INPUT_HEIGHT = 20;

    private final JTextArea textHistory;
    public boolean playerGivenBattleInput = false;

    public ConsoleWindow() {
        setupThisWindow();

        textHistory = setupTextHistory();

        JScrollPane scrollPane = setupScrollPane(textHistory);

        JLabel consoleLabel = setupConsoleLabel();

        JTextField textInput = setupTextInput(textHistory);

        this.add(consoleLabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(textInput, BorderLayout.SOUTH);
        //TODO: figure out how to give textInput focus (requestFocusInWindow() might be the way to go, but IDK how to use it)
    }

    private JLabel setupConsoleLabel() {
        JLabel textHistoryLabel = new JLabel("=|| CONSOLE ||=");
        textHistoryLabel.setForeground(Color.WHITE);
        textHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return textHistoryLabel;
    }

    /**
     * Sets up and returns a JTextField responsible for taking in user input.
     *
     * @param textHistory The JTextArea containing the input history.
     */
    private JTextField setupTextInput(JTextArea textHistory) {
        JTextField textInput = new JTextField();

        textInput.setBackground(Color.BLACK);
        textInput.setForeground(Color.WHITE);
        textInput.setCaretColor(Color.WHITE);

        textInput.setSize(WIDTH, INPUT_HEIGHT);
        textInput.setMaximumSize(new Dimension(WIDTH, INPUT_HEIGHT));
        textInput.setPreferredSize(new Dimension(WIDTH, INPUT_HEIGHT));

        textInput.addActionListener(new OnEnterAction(textHistory, textInput));

        return textInput;
    }

    /**
     * Sets up the scroll pane that will hold the text history area.
     */
    private JScrollPane setupScrollPane(JTextArea textHistory) {
        JScrollPane scrollPane = new JScrollPane(textHistory);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private JTextArea setupTextHistory() {
        JTextArea textHistory = new JTextArea();
        textHistory.setEditable(false);
        textHistory.setBackground(Color.BLACK);
        textHistory.setForeground(Color.WHITE);
        textHistory.setLineWrap(true); // area wraps when line width is exceeded
        textHistory.setWrapStyleWord(true); // wrapping is done by word rather than by character
        return textHistory;
    }

    private void setupThisWindow() {
        setSize(1280, 300);
        setMaximumSize(new Dimension(1280, 300));
        setPreferredSize(new Dimension(1280, 300));
        this.setBackground(Color.BLACK);
        setLayout(new BorderLayout());
    }

    /**
     * Add a given entry to the display in the console.
     *
     * @param entryToAdd The string entry to add. A newline will be added if the input does not end with one.
     */
    public void addEntryToHistory(String entryToAdd) {
        if (entryToAdd.isEmpty()) {
            return;
        }
        textHistory.append(entryToAdd);
        if (entryToAdd.charAt(entryToAdd.length() - 1) != '\n') {
            textHistory.append("\n");
        }
    }

    //TODO: not working, maybe add some sort of listener to the text area, when it changes scroll down to the bottom
    public void scrollToBottom() {

    }

    public void getPlayerInput(OnEnterAction onEnterAction, int chosenMove) {
        onEnterAction.chosenAction = chosenMove;
        playerGivenBattleInput = true;
    }
}

/**
 * Class representing the action to be taken when the enter key is pressed on the text input field.
 *
 * @author William Owens
 * @version 1.0
 */
class OnEnterAction extends AbstractAction {

    private final JTextArea textHistory;
    private final JTextField textInput;

    public int chosenAction;

    public OnEnterAction(JTextArea textHistory, JTextField textInput) {
        this.textHistory = textHistory;
        this.textInput = textInput;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (Main.gameOver) {
            return;
        }
        boolean battle = false;
        String inputString = e.getActionCommand();

        if (inputString.length() < 1) {
            return;
        }

        sendInputToHistory(inputString);

        try {
            int chosenMove = Integer.parseInt(inputString);
            if (Main.currentBattle != null) {
                battle = true;
                Main.mainWindow.getConsoleWindow().getPlayerInput(this, chosenMove);
                Main.currentBattle.player.setChosenAction(chosenMove);
                Main.currentBattle.player.wakeUp();
                // all of the thread stuff is kind of a horrible mess, it works though. clean it all up at some point
            } else {
                battle = false;
            }

        } catch (NumberFormatException exception) {
            if (Main.currentBattle != null) {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Nothing recognized in this input. Did you type everything correctly?");
                return;
            }
        }

        if (battle) {
            Main.currentBattle.player.setChosenAction(chosenAction);
            return;
        }

        switch (inputString.toLowerCase()) {
            case "tutorial" -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory(ConsoleWindow.TUTORIAL);
                return;
            }
            case "map" -> {
                Main.mainWindow.getViewportPanel().drawFloorMap(Main.currentFloor);
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Your player's position is represented by the cyan square.");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Close the map with the \"close map\" command.");
                return;
            }
            case "status" -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("---Your Status---");
                Main.player.printStatsToConsole();
                ArrayList<Feature> equipmentFeatures = Main.player.getPlayerAsFeature().getChildren(FeatureFlag.EQUIPPABLE);
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Gold: %d".formatted(Main.player.getGold()));
                for (Feature feature : equipmentFeatures) {
                    EquipmentFeature equipmentFeature = ((EquipmentFeature) feature);
                    if (equipmentFeature instanceof ArmorFeature) {
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You wear the %s, granting you a base max HP of %d.".formatted(equipmentFeature.getPrimaryName(), (long) (equipmentFeature).getValue()));
                    } else if (equipmentFeature instanceof ShieldFeature) {
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You bear the %s, granting you a base damage absorption of %.2f%%.".formatted(equipmentFeature.getPrimaryName(), (equipmentFeature).getValue() * 100));
                    } else if (equipmentFeature instanceof WeaponFeature) {
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You wield the %s, granting you a base damage of %d.".formatted(equipmentFeature.getPrimaryName(), (long) equipmentFeature.getValue()));
                    }
                }
                for (Feature feature : equipmentFeatures) {
                    EquipmentFeature equipmentFeature = ((EquipmentFeature) feature);
                    if (equipmentFeature instanceof AccessoryFeature) {
                        AccessoryFeature accessoryFeature = (AccessoryFeature) feature;
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You posses the %s. %s".formatted(accessoryFeature.getPrimaryName(), accessoryFeature.getEffectDescriptionForStatus()));
                    }
                }
                for (Feature feature : Main.player.getPlayerAsFeature().getChildren()) {
                    if (feature instanceof EquipmentFeature) {
                        continue;
                    }
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You have a %s.".formatted(feature.getPrimaryName()));
                }
                double elapsed = (System.nanoTime() - Main.timeStart) * 0.000000000017;
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You've been playing for about %.2f minutes.".formatted(elapsed));
                return;
            }
            case "map close", "close map" -> {
                Main.mainWindow.getViewportPanel().drawRoom(Main.player.getCurrentRoom().getRoomAsFeature());
                return;
            }
        }

        PlayerAction pa = Parser.getActionFromInput(inputString);
        if (pa == null) {
            Main.mainWindow.getConsoleWindow().addEntryToHistory("No recognized verb in this input. Did you spell everything correctly?");
            return;
        }
        Feature feature = Parser.getObjectFromInput(inputString);
        //should probably refactor this and other logic stuff to GameManager, or some other logic manager (InputManager, maybe)
        if (feature == null) {
            Main.mainWindow.getConsoleWindow().addEntryToHistory("Try as you might, you cannot see anything like that in this room...");
        } else {
            GameManager.executeAction(pa, feature);
        }
        Main.mainWindow.getConsoleWindow().scrollToBottom();
    }

    private void sendInputToHistory(String inputString) {
        textHistory.append(">");
        textHistory.append(inputString);
        textHistory.append("\n");
        textInput.setText("");
    }
}
