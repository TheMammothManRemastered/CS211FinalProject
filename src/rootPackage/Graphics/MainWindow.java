package rootPackage.Graphics;

import rootPackage.Graphics.GUI.ConsoleWindow;
import rootPackage.Graphics.Viewport.ViewportPanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;

/**
 * This class handles the main window for the game. On an upper level, this class basically is doing everything.
 *
 * @author William Owens
 * @version 0.2
 */
public class MainWindow {

    private JFrame window;

    public MainWindow() {
        window = new JFrame();

        start();
    }

    private void start() {
        window.setTitle("Game Time Started");
        window.setLayout(new BorderLayout()); // manages the way components are laid out on the window
        // hgap and vgap are padding distances in pixels, horizontal and vertical respectively
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // kills the whole program when closing
        window.setSize(1280, 820);
        window.setResizable(false);
        window.setLocationRelativeTo(null); // this centers the window by default

        ViewportPanel viewportPanel = new ViewportPanel();
        ConsoleWindow consoleWindow = new ConsoleWindow();

        // adds panel to the middle of the window's layout manager
        window.add(viewportPanel);
        window.add(consoleWindow, BorderLayout.SOUTH);

        window.setVisible(true);
    }


}
