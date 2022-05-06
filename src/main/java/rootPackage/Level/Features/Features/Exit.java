package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;
import rootPackage.Player;

public class Exit extends Feature {

    private boolean locked;
    private final String nameOfKey;

    public Exit() {
        this.locked = true;
        this.examineText = "The trapdoor to the next floor!";
        this.sprite = new Sprite(RenderLayer.FLOOR, "trapdoor.png");
        this.nameOfKey = "trapdoor key";
        this.primaryName = "Trapdoor";
        this.allNames = new String[]{"trapdoor", "exit"};
    }

    private boolean tryUnlock(Player player) {
        return (player.getPlayerAsFeature().getChildWithName(nameOfKey) != null);
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                String affix = (locked) ? "It's locked tight. You'll need some sort of key before trying to proceed." : "";
                Main.mainWindow.getConsoleWindow().addEntryToHistory("A trapdoor leading to the next floor. %s".formatted(affix));
            }
            case UNLOCK -> {
                boolean unlockAttempt = tryUnlock(Main.player);
                if (locked && unlockAttempt) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("The trapdoor makes an audible click. Looks like you've unlocked it!");
                    locked = false;
                    Main.player.getPlayerAsFeature().getChildWithName(nameOfKey).removeFromPlay();
                    sprite = new Sprite(RenderLayer.FLOOR, "trapdoorOpen.png");
                    return;
                }
                if (locked) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("The trapdoor stays locked. Seems you don't have the right item to open it...");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The trapdoor is already unlocked!");
            }
            case USE -> {
                if (locked) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("Try as you might, the trapdoor remains locked shut. Maybe you should unlock it first.");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You open the trapdoor and jump down into the depths below...");
                Main.moveToShop();
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
            case ATTACK -> {
                if (locked) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("Seems like hitting the trapdoor isn't helping; it's just as locked as it was before");
                    return;
                }
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking the unlocked trapdoor doesn't do an awful lot.");
            }
        }
    }
}
