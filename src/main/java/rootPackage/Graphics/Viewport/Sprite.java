package rootPackage.Graphics.Viewport;

import rootPackage.Graphics.GUI.RenderLayer;

public class Sprite implements Comparable<Sprite>{
    private RenderLayer renderLayer;
    private String imageName;

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

    public void setRenderLayer(RenderLayer renderLayer) {
        this.renderLayer = renderLayer;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public int compareTo(Sprite o) {
        return this.renderLayer.compareTo(o.renderLayer);
    }
}
