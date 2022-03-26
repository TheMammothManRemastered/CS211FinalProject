public enum Direction {
    NORTH (-1,0),
    SOUTH (1,0),
    EAST (0,1),
    WEST (0,-1),
    CENTER (0,0);

    public final int verticalMovement;
    public final int horizontalMovement;

    Direction(int verticalMovement, int horizontalMovement) {
        this.verticalMovement = verticalMovement;
        this.horizontalMovement = horizontalMovement;
    }

    /**
     * Returns an array of all Directions, except for CENTER.
     */
    public static Direction[] allButCenter() {
        return new Direction[] {Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST};
    }
}
