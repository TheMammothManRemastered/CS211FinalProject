package rootPackage.Level.Features.Enemy.Enemies;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Enemy.Enemy;
import rootPackage.Main;

public class Bear extends Enemy {

    public Bear() {
        super("Bear", new String[]{""});
        sprite = new Sprite(RenderLayer.ENEMY, "bear.png");
        examineText = "A large and very intimidating bear sits on the floor.";
        jsonFile = "json"+System.getProperty("file.separator")+"enemyStats"+System.getProperty("file.separator")+"bear.json";
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a bear.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You ready your weapon and charge at the bear!");
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("As you approach the bear, it growls and rears up, ready to attack!");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You definitely are not holding the bear right now.");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The bear isn't locked, so far as you know.");
            }
        }
    }

}