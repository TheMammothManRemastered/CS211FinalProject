package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;

public class Moss extends Feature {

    public Moss() {
        this(1);
    }

    public Moss(int number) {
        super("Moss", new String[]{"moss", "plants"});
        this.sprite = new Sprite(RenderLayer.WALL_DECO, "moss%d.png".formatted(number));
        this.examineText = "Soft, green moss is growing out of the walls.";
    }

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
