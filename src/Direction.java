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
}
