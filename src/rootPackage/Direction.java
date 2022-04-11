package rootPackage;

public enum Direction {
    NORTH (-1,0),
    SOUTH (1,0),
    EAST (0,1),
    WEST (0,-1),
    CENTER (0,0);

    public final int verticalOffset;
    public final int horizontalOffset;

    Direction(int verticalOffset, int horizontalOffset) {
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
    }

    /**
     * Returns an array of all Directions, except for CENTER.
     */
    public static Direction[] allButCenter() {
        return new Direction[] {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST};
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
            case CENTER -> CENTER;
        };
    }

    public static Direction degreesToDirection(double angle) {
        // -45 to 45 is north
        // 45 to 135 is east
        // 135 to 180 and -135 to -180 is south
        // -45 to -135 is west
        if (-45 <= angle && angle < 45) {
            return NORTH;
        }
        if (45 <= angle && angle < 135) {
            return EAST;
        }
        if ((135 <= angle && angle <= 180) || (-180 <= angle && angle <= -135)) {
            return SOUTH;
        }
        if (-135 < angle && angle < -45) {
            return WEST;
        }
        return CENTER;
    }


}
