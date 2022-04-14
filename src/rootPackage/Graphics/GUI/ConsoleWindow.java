package rootPackage.Graphics.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The ConsoleWindow class is a JPanel container holding the 'console' system.
 * This class is responsible for taking user input, showing the user's input history, and showing the user the result of their actions.
 * This class is NOT responsible for actually interpreting the user's input. Another class handles that. This is just the frontend.
 *
 * @author William Owens
 * @version 1.0
 */
public class ConsoleWindow extends JPanel {

    private final int WIDTH = 1280;
    private final int INPUT_HEIGHT = 20;

    // this area needs to be global, but the other ones do not
    private final JTextArea textHistory;

    public ConsoleWindow() {
        setupThisWindow();

        textHistory = setupTextHistory();

        JScrollPane scrollPane = setupScrollPane(textHistory);

        JLabel consoleLabel = setupConsoleLabel();

        JTextField textInput = setupTextInput(textHistory);

        this.add(consoleLabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(textInput, BorderLayout.SOUTH);
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
        textHistory.append(entryToAdd);
        if (entryToAdd.charAt(entryToAdd.length() - 1) != '\n') {
            textHistory.append("\n");
        }
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

    public OnEnterAction(JTextArea textHistory, JTextField textInput) {
        this.textHistory = textHistory;
        this.textInput = textInput;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String inputString = e.getActionCommand();
        if (inputString.length() < 1) {
            return;
        }
        sendInputToHistory(inputString);
    }

    private void sendInputToHistory(String inputString) {
        textHistory.append(">");
        textHistory.append(inputString);
        textHistory.append("\n");
        textInput.setText("");
    }
}
