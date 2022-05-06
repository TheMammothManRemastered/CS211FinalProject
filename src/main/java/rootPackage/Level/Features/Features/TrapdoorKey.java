package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

public class TrapdoorKey extends Feature {

    public TrapdoorKey() {
        super();
        this.primaryName = "Trapdoor Key";
        this.allNames = new String[]{"floor key"};
        this.sprite = new Sprite(RenderLayer.NOT_DRAWN, null);
        this.examineText = "";
        this.flags.add(FeatureFlag.INVISIBLE);
    }

    // trapdoor key cannot be interacted with
    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }

}
