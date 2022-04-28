package rootPackage.Graphics.Viewport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The ViewportPanel class is a JPanel container holding whatever graphical component is being displayed to the screen.
 *
 * @author William Owens
 * @version 0.1
 */
public class ViewportPanel extends JPanel {

    private final int WIDTH = 1280;
    private final int HEIGHT = 520;

    private BufferedImage combat;

    private ImageIcon icon;
    private JLabel jLabel;
    private JScrollPane scrollPane;

    public ViewportPanel() {
        start();
        try {
            combat = ImageIO.read(new File("img" + System.getProperty("file.separator") + "test_viewport_image.png"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void start() {
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        // to make an image scrollable, make an ImageIcon, make a JLabel holding the icon, add that to a scrollpane
        icon = new ImageIcon("img" + System.getProperty("file.separator") + "test_viewport_image.png");
        jLabel = new JLabel(icon);

        // some logic should be done on this, make the scroll pane only visible when the image is large enough to justify it (ie. the map screen)
        scrollPane = new JScrollPane(jLabel);

        this.add(scrollPane, BorderLayout.CENTER);

    }

    public void updateImage(String imageName) {
        try {
            combat = ImageIO.read(new File("img" + System.getProperty("file.separator") + "test_viewport_image_kill.png"));
            icon.setImage(combat);
            scrollPane.paint(scrollPane.getGraphics());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }



}
