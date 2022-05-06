package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

public class Bloodstain extends Feature {

    public Bloodstain() {
        this(1);
    }

    public Bloodstain(int number) {
        super("Bloodstain", new String[]{"bloodstain", "stain", "bloodstains", "stains", "blood"});
        this.sprite = new Sprite(RenderLayer.FLOOR, "bloodstain%d.png".formatted(number));
        this.examineText = "The floor is caked in blood.";
    }

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
