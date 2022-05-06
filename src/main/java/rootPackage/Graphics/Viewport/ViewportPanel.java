package rootPackage.Graphics.Viewport;

import rootPackage.Direction;
import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Features.Door;
import rootPackage.Level.Features.TopLevel.Room;
import rootPackage.Level.Floor;
import rootPackage.Level.FloorGeneration.Layout.MyPoint2D;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;
import rootPackage.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * The ViewportPanel class is a JPanel container holding whatever graphical component is being displayed to the screen.
 *
 * @author William Owens
 * @version 0.1
 */
public class ViewportPanel extends JPanel {

    private final int WIDTH = 1280;
    private final int HEIGHT = 520;

    private JScrollPane scrollPane;
    private JPanel canvas;
    private ImageIcon icon;

    public ViewportPanel() {
        start();
    }

    public void start() {
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        // to make an image scrollable, make an ImageIcon, make a JLabel holding the icon, add that to a scrollpane
        icon = new ImageIcon("img" + System.getProperty("file.separator") + "test_viewport_image.png");
        JLabel jLabel = new JLabel(icon);

        canvas = new JPanel();
        canvas.setBackground(Color.BLACK);

        scrollPane = new JScrollPane(canvas);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void drawImageOnCanvas(String imageName) {
        try {
            BufferedImage imageToDraw = ImageIO.read(new File("img" + System.getProperty("file.separator") + imageName));
            this.canvas.getGraphics().drawImage(imageToDraw, 0, 0, null);
        } catch (IOException exception) {
            System.out.printf("Image not found! %s%n", imageName);
        }
    }

    public void drawImageOnCanvas(Image image, int xOffset) {
        this.canvas.getGraphics().drawImage(image, xOffset, 0, null);
    }

    public void clearCanvas() {
        this.canvas.paint(this.canvas.getGraphics());
        canvas.createImage(WIDTH, HEIGHT);
    }

    public void drawRoom(Feature room) {
        ArrayList<Feature> features = new ArrayList<>(room.getChildren());
        features.add(room);
        ArrayList<Sprite> toDraw = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.getSprite().getRenderLayer() != RenderLayer.NOT_DRAWN) {
                toDraw.add(feature.getSprite());
            }
            if (!features.containsAll(feature.getChildren())) {
                features.addAll(feature.getChildren());
            }
        }
        Collections.sort(toDraw);
        for (Sprite s : toDraw) {
            System.out.println(s.getImageName());
        }
        Consumer<Sprite> spriteConsumer = (Sprite sprite) -> {
            Main.mainWindow.getViewportPanel().drawImageOnCanvas(sprite.getImageName());
        };
        toDraw.forEach(spriteConsumer);
    }

    public void drawFloorMap(Floor floor) {

        //TODO: draw an image into the scrollpane and have it be usable, not sure how to do that just yet without things breaking, look into the cardLayout layout manager
        this.canvas.setBackground(Color.BLACK);
        this.canvas.getGraphics().clearRect(0,0,WIDTH, HEIGHT);
        drawImageOnCanvas(floor.createFloorMap(), WIDTH/4);
        drawImageOnCanvas("greatest_compass_ever_made.png");
    }

}
