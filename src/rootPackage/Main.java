package rootPackage;

import rootPackage.FloorGeneration.Features.Door;
import rootPackage.FloorGeneration.Floor;
import rootPackage.FloorGeneration.FloorGenerator;
import rootPackage.FloorGeneration.Layout.Connection;
import rootPackage.FloorGeneration.Layout.MyPoint2D;
import rootPackage.FloorGeneration.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends JPanel {

	public static final int PIXELS_PER_CARTESIAN_POINT = 60;

	private static Floor floor;

	private BufferedImage compass;
	private Room spawnLocation;
	private Player player;

	public static void main(String[] args) {

		FloorGenerator fg = new FloorGenerator();
		floor = fg.generateFloor();

		Main man = new Main();

		JFrame frame = new JFrame("floor layout testing");
		frame.add(man);
		frame.setSize(800,800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			man.start();
			System.exit(1);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void start() throws Exception {
		Scanner scanner = new Scanner(System.in);
		boolean escape = false;
		System.out.println("welcome to the map demo. type a cardinal direction to select which way to go. type 'exit' to exit.");
		while (!escape) {
			String input = scanner.nextLine();
			switch (input.toLowerCase()) {
				case "north" -> {
					if (! (player.getCurrentRoom().hasDoorInDirection(Direction.NORTH))) {
						System.out.println("no room to the north");
						break;
					}
					// horrid one-liner, sets player's current room to the one in the specified direction
					player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.NORTH).getOtherSide().getAssociatedRoom());
				}
				case "south" -> {
					if (! (player.getCurrentRoom().hasDoorInDirection(Direction.SOUTH))) {
						System.out.println("no room to the south");
						break;
					}
					// horrid one-liner, sets player's current room to the one in the specified direction
					player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.SOUTH).getOtherSide().getAssociatedRoom());
				}
				case "east" -> {
					if (! (player.getCurrentRoom().hasDoorInDirection(Direction.EAST))) {
						System.out.println("no room to the east");
						break;
					}
					// horrid one-liner, sets player's current room to the one in the specified direction
					player.setCurrentRoom(player.getCurrentRoom().getDoorInDirection(Direction.EAST).getOtherSide().getAssociatedRoom());
				}
				case "west" -> {
					if (! (player.getCurrentRoom().hasDoorInDirection(Direction.WEST))) {
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
				default -> {
					System.out.println("input is not a cardinal direction, not processed");
				}
			}
			this.paint(this.getGraphics());
			System.out.println("type a cardinal direction to select which way to go. type 'exit' to exit.");
		}
	}

	public Main() {
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
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		Rectangle2D.Double fill = new Rectangle2D.Double(0,0,800,800);
		g2d.fill(fill);
		g2d.drawImage(compass,0,0,null);
		g2d.setColor(Color.YELLOW);
		g2d.drawString("welcome to the map demo. type a cardinal direction in the console to select which way to go. type 'exit' to exit.",150,80);
		g2d.setColor(Color.GRAY);

		ArrayList<Room> rooms = floor.getRooms();
		for (Room room : rooms) {
			MyPoint2D point = new MyPoint2D(room.getCoordinates());
			point.x *= PIXELS_PER_CARTESIAN_POINT;
			point.y *= PIXELS_PER_CARTESIAN_POINT;
			point.y = 800 - point.y;
			Rectangle2D.Double roomRect = new Rectangle2D.Double((point.x-15), (point.y-15), (30), (30));
			ArrayList<Door> doors = room.getDoors();
			ArrayList<Line2D> lines = new ArrayList<>();
			for (Door door: doors) {
				MyPoint2D otherPoint = new MyPoint2D(door.getOtherSide().getAssociatedRoom().getCoordinates());
				otherPoint.x *= PIXELS_PER_CARTESIAN_POINT;
				otherPoint.y *= PIXELS_PER_CARTESIAN_POINT;
				otherPoint.y = 800 - otherPoint.y;
				Direction originDirection = door.getDirection();
				Direction endDirection = door.getOtherSide().getDirection();
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
				Line2D.Double line = new Line2D.Double(point.x+xOffsetOrigin,point.y+yOffsetOrigin, otherPoint.x+xOffsetEnd, otherPoint.y+yOffsetEnd);
				lines.add(line);
			}
			g2d.draw(roomRect);
			for (Line2D line : lines) {
				g2d.draw(line);
			}
		}

		MyPoint2D playerLoc = new MyPoint2D(player.getCurrentRoom().getCoordinates());
		playerLoc.x *= PIXELS_PER_CARTESIAN_POINT;
		playerLoc.x -= 10;
		playerLoc.y *= PIXELS_PER_CARTESIAN_POINT;
		playerLoc.y = 800 - playerLoc.y;
		playerLoc.y -= 10;
		Rectangle2D.Double playerMarker = new Rectangle2D.Double(playerLoc.x,playerLoc.y,20,20);

		g2d.setColor(Color.BLUE);
		g2d.fill(playerMarker);

	}

}
