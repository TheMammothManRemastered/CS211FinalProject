package rootPackage.Level.Features.Features;

import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

public class Meat extends Feature {

    public Meat() {
        super();
        this.primaryName = "Meat";
        this.allNames = new String[]{"meats", "raw meat", "flesh"};
        this.sprite = new Sprite(RenderLayer.OTHER, "meats.png");
        this.examineText = "";
        this.flags.add(FeatureFlag.INVISIBLE);
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You pick up the meat, taking care to not get too much blood on your hands.");
                Main.player.getPlayerAsFeature().addChild(this);
            }
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Chunks of red, bloody meat. It looks disgusting, but, if you were a beast you'd probably think this a fine meal.");
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You're not sure why, but you don't think you should drop this...");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The meat isn't locked, shockingly.");
            }
            case USE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You really, really don't want to eat this.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking raw meat? Whatever for?");
            }
            case EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("What? Are you going to put it on your head like a hat or something? Gross!");
            }
        }
    }
}
