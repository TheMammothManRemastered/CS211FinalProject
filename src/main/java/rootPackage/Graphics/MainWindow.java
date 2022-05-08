package rootPackage.Graphics;

import org.json.simple.*;
import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Graphics.Viewport.ViewportPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This class handles the main window for the game.
 *
 * @author William Owens
 * @version 1.0
 */
public class MainWindow {

    private final JFrame window;
    private ViewportPanel viewportPanel;
    private ConsoleWindow consoleWindow;

    public MainWindow() {
        window = new JFrame();

        start();
    }

    private void start() {
        window.setTitle("Game Time Started"); // TODO: come up with a title for this lol
        window.setLayout(new BorderLayout()); // manages the way components are laid out on the window
        // borderLayout has components connected to the borders of the window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // kills the whole program when closing
        window.setSize(1280, 820);
        window.setResizable(false);
        window.setLocationRelativeTo(null); // this centers the window by default

        viewportPanel = new ViewportPanel();
        consoleWindow = new ConsoleWindow();

        // adds panel to the middle of the window's layout manager
        window.add(viewportPanel);
        // adds panel to the bottom of the window's layout manager
        window.add(consoleWindow, BorderLayout.SOUTH);

        window.setVisible(true);
    }

    public ViewportPanel getViewportPanel() {
        return viewportPanel;
    }

    public ConsoleWindow getConsoleWindow() {
        return consoleWindow;
    }
}
