package rootPackage.Level.Features.Features;

import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

public class Table extends Feature {

    public Table() {
        super();
        this.primaryName = "Table";
        this.allNames = new String[]{"wooden table", "table"};
        this.sprite = new Sprite(RenderLayer.OTHER, "table.png");
        this.examineText = "A sturdy wooden table lies in the corner. Is there something on it?";
    }

    @Override
    public void react(PlayerAction playerAction) {
        if (playerAction == PlayerAction.EXAMINE) {
            String output = "A wooden table, pretty well made by the looks of it. ";
            if (this.getChildren().size() > 0) {
                output += "There's a %s on it. ".formatted(this.getChildren().get(0).getPrimaryName());
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The table won't budge. You look down and notice that it's been glued to the floor.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You aren't holding the table!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The table isn't locked.");
            }
            case USE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You sit down at the table for a nice meal, before remembering you have an important mission to accomplish.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The table shatters into pieces! Its contents fall to the ground!");
                for (Feature child : this.getChildren()) {
                    this.getParent().addChild(child);
                }
            }
        }
    }
}
