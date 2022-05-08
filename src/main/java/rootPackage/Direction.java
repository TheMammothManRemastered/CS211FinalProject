package rootPackage;

import org.json.simple.*;
import rootPackage.Level.FloorGeneration.Layout.Connection;

/**
 * An enum representing one of the four cardinal directions.
 *
 * @author William Owens
 * @version 3.0
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

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
        } else if (45 <= angle && angle < 135) {
            return EAST;
        } else if ((135 <= angle && angle <= 180) || (-180 <= angle && angle <= -135)) {
            return SOUTH;
        } else if (-135 < angle && angle < -45) {
            return WEST;
        }
        return null;
    }

    /**
     * Converts an int number to a direction.
     *
     * @param i 0, 1, 2 or 3.
     * @return NORTH, SOUTH, EAST, or WEST respectively.
     *
     * @throws IndexOutOfBoundsException If the input is less than 0 or greater than 3.
     */
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
                throw new IndexOutOfBoundsException("numToDirection does not accept numbers less than 0 or greater than 3");
            }
        }
    }

    /**
     * Returns a direction's 'offset', the 2d 'vector' representing the orientation this direction represents in terms of graphics.
     *
     * @return An array of two ints, the first element is the horizontal component, and the second is the vertical component.
     */
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
            default -> {
                return new int[]{0, 0};
            }
        }
    }


}
