package rootPackage.FloorGeneration.Layout;

import rootPackage.Direction;

import java.util.ArrayList;

/**
 * A class representing the layout of a floor.
 *
 * @author William Owens
 * @version 1.0
 */
public class FloorLayout {

    private ProtoRoom[] rooms;

    public FloorLayout(ProtoRoom... rooms) {
        this.rooms = rooms;
    }

    public ProtoRoom[] getRooms() {
        return rooms;
    }

    /**
     * A method made for demonstrative purposes only. It should not be called under normal circumstances.
     * Prints this FloorLayout in the formatting that the website desmos uses.
     */
    public void printFloorDesmos() {
        ArrayList<MyPoint2D> pointsSeen = new ArrayList<>();
        char pointLetterIterator = 'A';
        Direction[] directions = Direction.allButCenter();
        for (ProtoRoom room : rooms) {

            if (!(pointsSeen.contains(room.getCoordinates()))) {
                pointsSeen.add(room.getCoordinates());
                System.out.printf("\\left(x-%f\\right)^{2}+\\left(y-%f\\right)^{2}=0.25^{2}%n", room.getCoordinates().x, room.getCoordinates().y);
                pointLetterIterator++;
            }

            for (Direction dir : directions) {
                if (room.getRoom(dir) == null) {
                    continue;
                }
                if (room.getRoom(dir).getCoordinates().equals(room.getCoordinates())) {
                    continue;
                }
                if (pointsSeen.contains(room.getRoom(dir).getCoordinates())) {
                    continue;
                }
                MyPoint2D roomCoords = new MyPoint2D(room.getCoordinates());
                MyPoint2D otherCoords = new MyPoint2D(room.getRoom(dir).getCoordinates());
                ProtoRoom otherRoom = room.getRoom(dir);
                Direction dirOther = dir.opposite();
                // get other room's direction to this one
                for (Direction direction : directions) {
                    if (otherRoom.getRoom(direction) == null) {
                        continue;
                    }
                    if (otherRoom.getRoom(direction).equals(room)) {
                        dirOther = direction;
                        break;
                    }
                }

                switch (dirOther) {

                    case NORTH -> {
                        otherCoords.y+=0.25;
                    }
                    case SOUTH -> {
                        otherCoords.y-=0.25;
                    }
                    case EAST -> {
                        otherCoords.x+=0.25;
                    }
                    case WEST -> {
                        otherCoords.x-=0.25;
                    }
                    default -> {
                    }
                }

                switch (dir) {

                    case NORTH -> {
                        roomCoords.y += 0.25;
                        Connection con = new Connection(roomCoords, otherCoords);
                        con.printTriangleSides();
                    }
                    case SOUTH -> {
                        roomCoords.y -= 0.25;
                        Connection con = new Connection(roomCoords, otherCoords);
                        con.printTriangleSides();
                    }
                    case EAST -> {
                        roomCoords.x += 0.25;
                        Connection con = new Connection(roomCoords, otherCoords);
                        con.printTriangleSides();
                    }
                    case WEST -> {
                        roomCoords.x -= 0.25;
                        Connection con = new Connection(roomCoords, otherCoords);
                        con.printTriangleSides();
                    }
                    default -> {
                    }
                }
            }

        }

    }
}
