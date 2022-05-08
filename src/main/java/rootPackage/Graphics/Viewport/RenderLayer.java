package rootPackage.Graphics.Viewport;

import org.json.simple.*;

/**
 * Enum representing the layers and ordering of sprite rendering.
 *
 * @author William Owens
 * @version 1.0
 */
public enum RenderLayer implements Comparable<RenderLayer>{
    NOT_DRAWN,
    WALLS,
    WALL_DECO,
    DOORS,
    FLOOR,
    OTHER,
    ENEMY
}
