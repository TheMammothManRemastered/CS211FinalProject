package rootPackage.Level.Features.Features;

import org.json.simple.*;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;

public class TrapdoorKey extends Feature {

    public TrapdoorKey() {
        super();
        this.primaryName = "Trapdoor Key";
        this.allNames = new String[]{"floor key"};
        this.sprite = new Sprite(RenderLayer.NOT_DRAWN, null);
        this.examineText = "";
        this.flags.add(FeatureFlag.INVISIBLE);
    }

    // TODO: these
    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }

}
