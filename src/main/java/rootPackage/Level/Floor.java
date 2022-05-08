package rootPackage.Level;

import org.json.simple.*;
import rootPackage.Direction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Features.Door;
import rootPackage.Level.FloorGeneration.Layout.MyPoint2D;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;
import rootPackage.Main;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Represents a floor.
 *
 * @author William Owens
 * @version 4.0
 */
public class Floor {

    private static final int PIXELS_PER_CARTESIAN_POINT = 10;
    private ArrayList<RoomNode> rooms;

    public Floor(ArrayList<RoomNode> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<RoomNode> getRooms() {
        return rooms;
    }

    /**
     * Gets and returns the {@link rootPackage.Level.FloorGeneration.Layout.RoomNode RoomNode} the player should spawn in.
     */
    public RoomNode getSpawn() {
        for (RoomNode room : rooms) {
            if (room.isSpawn()) {
                return room;
            }
        }
        return null;
    }

    /**
     * Creates and returns an {@link Image Image} of the floor's map.
     */
    //TODO: a bunch of stuff, much of it aesthetic
    // maybe redo this so the floor's map is written to the filesystem as an actual image, and it is referenced via it's filepath stored here as a constant
    // might help solve the scrolling issue, and saves performance too probably
    // redo the way hallways are drawn. make it more evident what direction they are in
    // mark important rooms. this can probably be encapsulated in RoomNode, if the room is a boss, miniboss, has a health point, has an enemy, etc. mark it somehow
    // only reveal to the player the rooms they have seen and their neighbors. Don't say what's in each room until the player actually goes in there.
    // could be implemented in RoomNode with isVisited and isSeen flag variables
    public Image createFloorMap() {
        RoomNode[] roomNodes = this.getRooms().toArray(new RoomNode[this.getRooms().size()]);
        Image output = Main.mainWindow.getViewportPanel().createImage(PIXELS_PER_CARTESIAN_POINT * 50 + PIXELS_PER_CARTESIAN_POINT, PIXELS_PER_CARTESIAN_POINT * 50 + PIXELS_PER_CARTESIAN_POINT);
        Graphics2D canvasGraphics = (Graphics2D) output.getGraphics();
        canvasGraphics.setBackground(Color.BLACK);
        canvasGraphics.clearRect(0, 0, PIXELS_PER_CARTESIAN_POINT * 50 + PIXELS_PER_CARTESIAN_POINT, PIXELS_PER_CARTESIAN_POINT * 50 + PIXELS_PER_CARTESIAN_POINT);
        canvasGraphics.setColor(Color.WHITE);
        for (RoomNode roomNode : roomNodes) {
            canvasGraphics.drawRect((int) roomNode.getCoordinates().x * PIXELS_PER_CARTESIAN_POINT - PIXELS_PER_CARTESIAN_POINT / 4,
                    (int) ((50 * PIXELS_PER_CARTESIAN_POINT + PIXELS_PER_CARTESIAN_POINT) - (roomNode.getCoordinates().y * PIXELS_PER_CARTESIAN_POINT + PIXELS_PER_CARTESIAN_POINT / 4)),
                    PIXELS_PER_CARTESIAN_POINT / 2,
                    PIXELS_PER_CARTESIAN_POINT / 2);
            ArrayList<Feature> doors = roomNode.getRoomAsFeature().getChildren(FeatureFlag.DOOR);
            for (Feature feature : doors) {
                Door door = (Door) feature;
                Direction thisDirection = door.getPositionOnWall();
                MyPoint2D thisCoords = new MyPoint2D(roomNode.getCoordinates());
                thisCoords.x *= PIXELS_PER_CARTESIAN_POINT;
                thisCoords.y *= PIXELS_PER_CARTESIAN_POINT;
                thisCoords.y = (50 * PIXELS_PER_CARTESIAN_POINT + PIXELS_PER_CARTESIAN_POINT) - thisCoords.y;
                thisCoords.x += PIXELS_PER_CARTESIAN_POINT * thisDirection.toOffset()[0] / 4.0;
                thisCoords.y -= PIXELS_PER_CARTESIAN_POINT * thisDirection.toOffset()[1] / 4.0;
                Direction otherDirection = door.getPositionOnWallOfOtherSide();
                MyPoint2D otherCoords = new MyPoint2D(door.getConnectedRoom().getCoordinates());
                otherCoords.x *= PIXELS_PER_CARTESIAN_POINT;
                otherCoords.y *= PIXELS_PER_CARTESIAN_POINT;
                otherCoords.y = (50 * PIXELS_PER_CARTESIAN_POINT + PIXELS_PER_CARTESIAN_POINT) - otherCoords.y;
                otherCoords.x += (PIXELS_PER_CARTESIAN_POINT * otherDirection.toOffset()[0]) / 4.0;
                otherCoords.y -= (PIXELS_PER_CARTESIAN_POINT * otherDirection.toOffset()[1]) / 4.0;
                if (door.getConnectedRoom().equals(roomNode)) {
                    continue;
                }
                canvasGraphics.drawLine((int) thisCoords.x, (int) thisCoords.y, (int) otherCoords.x, (int) otherCoords.y);
            }
        }
        MyPoint2D playerCoords = new MyPoint2D(Main.player.getCurrentRoom().getCoordinates());
        playerCoords.x *= PIXELS_PER_CARTESIAN_POINT;
        playerCoords.y *= PIXELS_PER_CARTESIAN_POINT;
        playerCoords.y = (50 * PIXELS_PER_CARTESIAN_POINT + PIXELS_PER_CARTESIAN_POINT) - playerCoords.y;
        canvasGraphics.setColor(Color.CYAN);
        Rectangle2D rectangle2D = new Rectangle2D.Double(playerCoords.x - 10, playerCoords.y - 10, PIXELS_PER_CARTESIAN_POINT + 10, PIXELS_PER_CARTESIAN_POINT + 10);
        canvasGraphics.draw(rectangle2D);
        canvasGraphics.dispose();
        return output;
    }
}
