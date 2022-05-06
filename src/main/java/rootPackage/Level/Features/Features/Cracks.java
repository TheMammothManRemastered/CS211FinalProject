package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;

public class Cracks extends Feature {

    public Cracks() {
        this(1);
    }

    public Cracks(int number) {
        super("Cracks", new String[]{"cracks", "wall", "walls"});
        this.sprite = new Sprite(RenderLayer.WALL_DECO, "crack%d.png".formatted(number));
        this.flags.add(FeatureFlag.INVISIBLE);
    }

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }

}
