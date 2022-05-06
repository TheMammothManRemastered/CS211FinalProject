package rootPackage;

import rootPackage.Level.Features.Equipment.WeaponFeature;
import rootPackage.Level.FloorGeneration.Layout.Connection;

/**
 * An enum representing one of the four cardinal directions, as well as a CENTER direction, representing no direction at all.
 *
 * @version 2.4
 * @author William Owens
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    CENTER;

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

    /**
     * Converts the degree measurement outputted via {@link rootPackage.Level.FloorGeneration.Layout.Connection Connection}'s {@link Connection#getRelativeDegrees() getRelativeDegrees} method into a Direction.
     */
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

    public static Direction numToDirection(int i) {
        switch (i) {
            case 0 -> {
                return NORTH;
            }
            case 1 -> {
                return SOUTH;
            }
            case 2 -> {
                return EAST;
            }
            case 3 -> {
                return WEST;
            }
            default -> {
                return CENTER;
            }
        }
    }

    public int[] toOffset() {
        switch (this) {
            case NORTH -> {
                return new int[]{0, 1};
            }
            case WEST -> {
                return new int[]{-1, 0};
            }
            case SOUTH -> {
                return new int[]{0, -1};
            }
            case EAST -> {
                return new int[]{1, 0};
            }
            default-> {
                return new int[]{0, 0};
            }
        }
    }


}
