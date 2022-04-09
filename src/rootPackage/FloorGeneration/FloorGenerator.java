package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Features.Door;
import rootPackage.FloorGeneration.Layout.Connection;
import rootPackage.FloorGeneration.Layout.FloorLayout;
import rootPackage.FloorGeneration.Layout.ProtoRoom;

import java.util.ArrayList;
import java.util.List;

public class FloorGenerator {

    private FloorLayout layout;

    public FloorGenerator(FloorLayout layout) {
        this.layout = layout;
    }

    public Floor generateFloor() {

        // fill the floor with all the relevant rooms
        ArrayList<Room> rooms = new ArrayList<>();
        ProtoRoom[] protoRooms = layout.getRooms();
        for (ProtoRoom protoRoom : protoRooms) {
            Room room = new Room(protoRoom);
            rooms.add(room);
        }

        // now, set up doors between the rooms
        Direction[] directions = Direction.allButCenter();
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            ProtoRoom protoRoom = protoRooms[i];
            for (Direction dir : directions) {
                if (protoRoom.getRoom(dir) == null) {
                    continue;
                }
                if (room.hasDoorInDirection(dir)) {
                    continue;
                }

                //TODO: debug location until this gets standardized
                Door thisSideDoor = new Door("wall"+dir,room);
                // get other room's direction to this one
                ProtoRoom otherProtoRoom = protoRoom.getRoom(dir);
                Direction dirOther = dir.opposite();
                for (Direction direction : directions) {
                    if (otherProtoRoom.getRoom(direction) == null) {
                        continue;
                    }
                    if (otherProtoRoom.getRoom(direction).equals(protoRoom)) {
                        dirOther = direction;
                        break;
                    }
                }
                int otherRoomIndex = List.of(protoRooms).indexOf(otherProtoRoom);
                Door otherSideDoor = new Door("wall"+dirOther,rooms.get(otherRoomIndex));
                thisSideDoor.setOtherSide(otherSideDoor);
                otherSideDoor.setOtherSide(thisSideDoor);
                room.getFeatures().add(thisSideDoor);
                rooms.get(otherRoomIndex).getFeatures().add(otherSideDoor);
            }
        }


        return new Floor();
    }

}
