package rootPackage.Level.Features.Enemy.Bosses;

import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Enemy.Enemy;
import rootPackage.Main;

public class Minotaur extends Enemy {

    public Minotaur() {
        super("Minotaur", new String[]{"minotaur", "foe", "enemy"});
        sprite = new Sprite(RenderLayer.ENEMY, "minotaur.png");
        examineText = "A horrifying thing stands in the middle of the room, snacking on a piece of raw meat. It looks like a man, but it's head is that of a bull. This must be the legendary Minotaur! It hasn't noticed you yet... perfect opportunity for a sneak attack!";
        jsonFile = "json" + System.getProperty("file.separator") + "enemyStats" + System.getProperty("file.separator") + "minotaur.json";
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's the minotaur, half man, half bull. It seems to be holding a distressingly large axe.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You ready your weapon and charge at the minotaur!");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
            default -> {
                onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP, USE, EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("As you approach the minotaur, it sees you and lets out a growl, raising its axe.");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You definitely are not holding the minotaur right now.");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You stick a lockpick into the minotaur's back, but no hidden door or anything appears. Instead, the minotaur growls and swings its axe!");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
        }
    }
}
