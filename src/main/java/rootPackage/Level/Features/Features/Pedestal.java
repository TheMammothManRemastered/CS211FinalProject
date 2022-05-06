package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

import java.util.Arrays;

public class Pedestal extends Feature {

    public Pedestal() {
        super();
        this.primaryName = "Pedestal";
        this.allNames = new String[]{"plinth", "pedestal", "pillar"};
        this.sprite = new Sprite(RenderLayer.OTHER, "pedestal.png");
        this.examineText = "A pedestal stands proud in the middle of the room. Is there something on it?";
    }

    @Override
    public void react(PlayerAction playerAction) {
        if (playerAction == PlayerAction.EXAMINE) {
            String output = "A opulent marble pedestal. ";
            if (this.getChildren().size() > 0) {
                System.out.println(Arrays.toString(this.getChildren().toArray()));
                output += "There's a %s on it.".formatted(this.getChildren().get(0).getPrimaryName());
            } else {
                output += "There's nothing on it.";
            }
            Main.mainWindow.getConsoleWindow().addEntryToHistory(output);
        } else {
            onActionNotApplicable(playerAction);
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP, EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You can't seem to separate the pillar from the ground.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You aren't holding the pedestal!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The pedestal isn't locked.");
            }
            case USE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("How would you use a pedestal?");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Your attacks don't even scratch the pedestal! Solid craftsmanship...");
            }
        }
    }
}
