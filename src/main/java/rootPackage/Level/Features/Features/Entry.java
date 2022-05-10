package rootPackage.Level.Features.Features;

import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

public class Entry extends Feature {

    public Entry() {
        this.examineText = "The trapdoor to the next floor!";
        this.sprite = new Sprite(RenderLayer.FLOOR, "trapdoorOpen.png");
        this.primaryName = "Trapdoor";
        this.allNames = new String[]{"trapdoor", "exit"};
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("A trapdoor leading to the next floor.");
            }

            case USE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You open the trapdoor and jump down into the depths below...");
                Main.moveToNewFloor();
            }
            default -> {
                onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP, EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Even if you could get this trapdoor out of its frame, you're not sure if you could carry it very far.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You're not carrying the trapdoor!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The trapdoor isn't locked!");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking the trapdoor doesn't do an awful lot.");
            }
        }
    }
}
