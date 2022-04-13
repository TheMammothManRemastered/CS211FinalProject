package rootPackage;

import rootPackage.FloorGeneration.Features.Feature;
import rootPackage.FloorGeneration.Floor;
import rootPackage.FloorGeneration.Room;
import rootPackage.Graphics.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class will, eventually, be the 'starter' class that launches the whole game.
 * Currently, it is responsible for running the floor generation demo.
 *
 * @author William Owens
 * @version 0.2
 */
public class Main extends JPanel {

    public static final int PIXELS_PER_CARTESIAN_POINT = 60;

    private static Floor floor;

    private BufferedImage compass;
    private Room spawnLocation;
    private Player player;

    public static void main(String[] args) {

        MainWindow mainWindow = new MainWindow();
    }

    /**
     * Run this off of an instance of this class to start the demo.
     *
     * @throws Exception If something goes wrong with getting a door (which can't actually happen in this context)
     */
    public void start() throws Exception {
        // set up a scanner to take player input.
        Scanner scanner = new Scanner(System.in);
        System.out.println("welcome to the map demo. type a cardinal direction to select which way to go. type 'exit' to exit.");

        // main loop, terminates when the player types "exit"
        while (true) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "north" -> {
                    if (!(player.getCurrentRoom().hasDoorInDirection(Direction.NORTH))) {
                        System.out.println("no room to the north");
                        break;
                    }
                    // horrid one-liner, sets player's current room to the one in the specified direction
                    player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.NORTH).getOtherSide().getAssociatedRoom());
                }
                case "south" -> {
                    if (!(player.getCurrentRoom().hasDoorInDirection(Direction.SOUTH))) {
                        System.out.println("no room to the south");
                        break;
                    }
                    // horrid one-liner, sets player's current room to the one in the specified direction
                    player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.SOUTH).getOtherSide().getAssociatedRoom());
                }
                case "east" -> {
                    if (!(player.getCurrentRoom().hasDoorInDirection(Direction.EAST))) {
                        System.out.println("no room to the east");
                        break;
                    }
                    // horrid one-liner, sets player's current room to the one in the specified direction
                    player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.EAST).getOtherSide().getAssociatedRoom());
                }
                case "west" -> {
                    if (!(player.getCurrentRoom().hasDoorInDirection(Direction.WEST))) {
                        System.out.println("no room to the west");
                        break;
                    }
                    // horrid one-liner, sets player's current room to the one in the specified direction
                    player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.WEST).getOtherSide().getAssociatedRoom());
                }
                case "exit" -> {
                    System.out.println("exiting...");
                    return;
                }
                case "examine" -> {
                    Room currentRoom = player.getCurrentRoom();
                    System.out.println(currentRoom.getRoomDescription());
                    ArrayList<Feature> features = currentRoom.getFeatures();
                    for (Feature feature : features) {
                        feature.onExamine(player);
                    }
                }
                default -> {
                    System.out.println("input unrecognized, not processed");
                }
            }
            // re-paint every time a new input is given
            this.paint(this.getGraphics());
            System.out.println("type a cardinal direction to select which way to go. type 'exit' to exit.");
        }
    }

    /**
     * Constructor.
     * Puts the player in the correct spawn room.
     */
    public Main() {


		/*


		for (Room room : floor.getRooms()) {
			if (room.isSpawnRoom()) {
				spawnLocation = room;
			}
		}
		this.player = new Player(spawnLocation);
		try {
			this.compass = ImageIO.read(new File("img"+System.getProperty("file.separator")+"greatest_compass_ever_made.png"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		 */
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


		/*


		// draw the singular greatest compass png ever created
		g2d.drawImage(compass,0,0,null);

		// change the color to yellow for the text. again, this isn't retroactive. the background stays black.
		g2d.setColor(Color.YELLOW);
		g2d.drawString("welcome to the map demo. type a cardinal direction in the console to select which way to go. type 'exit' to exit.",150,80);

		// set to gray for the rooms
		g2d.setColor(Color.GRAY);

		ArrayList<Room> rooms = floor.getRooms();
		for (Room room : rooms) {
			// point being the center point of the room. adjust the coordinates of the room to be drawn on the screen
			MyPoint2D point = new MyPoint2D(room.getCoordinates());
			point.x *= PIXELS_PER_CARTESIAN_POINT;
			point.y *= PIXELS_PER_CARTESIAN_POINT;
			point.y = 800 - point.y;
			// rectangle representing the room
			Rectangle2D.Double roomRect = new Rectangle2D.Double((point.x-15), (point.y-15), (30), (30));

			ArrayList<Door> doors = room.getDoors(); // doors in this room
			ArrayList<Line2D> lines = new ArrayList<>(); // will store the lines to be drawn that represent hallways
			for (Door door: doors) {
				// other point being the center point of the room this door leads to. same adjustments as above for point.
				MyPoint2D otherPoint = new MyPoint2D(door.getOtherSide().getAssociatedRoom().getCoordinates());
				otherPoint.x *= PIXELS_PER_CARTESIAN_POINT;
				otherPoint.y *= PIXELS_PER_CARTESIAN_POINT;
				otherPoint.y = 800 - otherPoint.y;

				Direction originDirection = door.getDirection(); // what wall of the room is this door on
				Direction endDirection = door.getOtherSide().getDirection(); // what wall of the other room does the hallway lead to

				// set offsets. the line representing the hallway will be drawn offset from the center of the room, so on the proper wall
				int xOffsetOrigin = 0;
				int yOffsetOrigin = 0;
				int xOffsetEnd = 0;
				int yOffsetEnd = 0;
				switch (originDirection) {
					case NORTH -> {
						yOffsetOrigin = -15;
					}
					case SOUTH -> {
						yOffsetOrigin = 15;
					}
					case EAST -> {
						xOffsetOrigin = 15;
					}
					case WEST -> {
						xOffsetOrigin = -15;
					}
					default -> {
					}
				}
				switch (endDirection) {
					case NORTH -> {
						yOffsetEnd = -15;
					}
					case SOUTH -> {
						yOffsetEnd = 15;
					}
					case EAST -> {
						xOffsetEnd = 15;
					}
					case WEST -> {
						xOffsetEnd = -15;
					}
					default -> {
					}
				}

				// create the hallway line with proper offsets, add it to the list of lines
				Line2D.Double line = new Line2D.Double(point.x+xOffsetOrigin,point.y+yOffsetOrigin, otherPoint.x+xOffsetEnd, otherPoint.y+yOffsetEnd);
				lines.add(line);
			}
			// draw the room, draw each of its hallways
			g2d.draw(roomRect);
			for (Line2D line : lines) {
				g2d.draw(line);
			}
		}

		// get the location of the player, same adjustments as to room coordinates
		MyPoint2D playerLoc = new MyPoint2D(player.getCurrentRoom().getCoordinates());
		playerLoc.x *= PIXELS_PER_CARTESIAN_POINT;
		playerLoc.x -= 10;
		playerLoc.y *= PIXELS_PER_CARTESIAN_POINT;
		playerLoc.y = 800 - playerLoc.y;
		playerLoc.y -= 10;
		// blue rectangle representing the player
		Rectangle2D.Double playerMarker = new Rectangle2D.Double(playerLoc.x,playerLoc.y,20,20);

		g2d.setColor(Color.BLUE);
		g2d.fill(playerMarker);


		 */
    }

}
