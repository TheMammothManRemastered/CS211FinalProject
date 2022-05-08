package rootPackage.Level.Features.Features;

import org.json.simple.*;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;

public class Bloodstain extends Feature {

    public Bloodstain() {
        this(1);
    }

    public Bloodstain(int number) {
        super("Bloodstain", new String[]{"bloodstain", "stain", "bloodstains", "stains", "blood"});
        this.sprite = new Sprite(RenderLayer.FLOOR, "bloodstain%d.png".formatted(number));
        this.examineText = "The floor is caked in blood.";
    }

    //TODO: implement these
    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
