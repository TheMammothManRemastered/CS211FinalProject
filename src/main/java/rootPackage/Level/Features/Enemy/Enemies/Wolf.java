package rootPackage.Level.Features.Enemy.Enemies;

import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Enemy.Enemy;
import rootPackage.Main;

public class Wolf extends Enemy {

    public Wolf() {
        super("Wolf", new String[]{});
        sprite = new Sprite(RenderLayer.ENEMY, "wolf.png");
        examineText = "A hungry wolf is examining the room for a meal...";
        jsonFile = "json" + System.getProperty("file.separator") + "enemyStats" + System.getProperty("file.separator") + "wolf.json";
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a wolf.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You ready your weapon and charge at the wolf!");
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("As you approach the wolf, it snarls and pounces forward!");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You definitely are not holding that wolf right now.");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The wolf isn't locked, so far as you know.");
            }
        }
    }

}