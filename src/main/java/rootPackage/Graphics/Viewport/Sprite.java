package rootPackage.Graphics.Viewport;

import org.json.simple.*;

/**
 * Class representing the sprite of a feature. Contains the feature's rendering layer and the image that will be drawn to the screen.
 *
 * @author William Owens
 * @version 1.0
 */
public class Sprite implements Comparable<Sprite>{
    private RenderLayer renderLayer;
    private final String imageName;

    public Sprite() {
        this.renderLayer = RenderLayer.NOT_DRAWN;
        this.imageName = null;
    }

    public Sprite(RenderLayer renderLayer, String imageName) {
        this.renderLayer = renderLayer;
        this.imageName = imageName;
    }

    public RenderLayer getRenderLayer() {
        return renderLayer;
    }

    public String getImageName() {
        return imageName;
    }

    public void setRenderLayer(RenderLayer renderLayer) {
        this.renderLayer = renderLayer;
    }

    @Override
    public int compareTo(Sprite o) {
        return this.renderLayer.compareTo(o.renderLayer);
    }
}
