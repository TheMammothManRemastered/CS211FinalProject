package rootPackage.Level.Features.Features;

import org.json.simple.*;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

public class HealthPoint extends Feature {

    boolean used = false;

    public HealthPoint() {
        super();
        this.primaryName = "Statue of Aphrodite";
        this.allNames = new String[]{"statue", "aphrodite", "bust", "health point"};
        this.sprite = new Sprite(RenderLayer.OTHER, "healthPoint.png");
        this.examineText = "A statue of Aphrodite lies in the corner, bathed in some miraculous light. If memory serves, these can heal your wounds!";
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                String output = "A statue of Aphrodite, the Goddess of Love. ";
                if (used) {
                    output += "Judging by the lack of divine light, the statue won't be able to heal you.";
                } else {
                    output += "In her arms is a glowing orb of brilliant, divine light. Just looking at it makes you feel much better!";
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory(output);
            }
            case USE -> {
                if (used) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You've already used this statue, it will have no effect.");
                } else {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You kneel in prayer in front of the statue. You can feel a glorious light enveloping you. The pain in your body vanishes!");
                    Main.player.setCurrentHP(Main.player.getCurrentHP() + 50);
                    used = true;
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("As you get up again, you see the light from the statue fade away.");
                    this.sprite = new Sprite(RenderLayer.OTHER, "healthPointUsed.png");
                }
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("While removing the statue from this wretched dungeon is certainly a great idea, you know that you cannot leave until your target is slain.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You aren't holding the statue!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The statue isn't locked.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You would attack the visage of a god? Heresy!");
            }
        }
    }

}
