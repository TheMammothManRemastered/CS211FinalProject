package rootPackage.Level.Features.Enemy.Enemies;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Enemy.Enemy;
import rootPackage.Main;

public class WolfPack extends Enemy {

    public WolfPack() {
        super("Wolf Pack", new String[]{"pack", "wolves", "wolfs", "pack wolves", "pack wolf", "pack wolfs"});
        sprite = new Sprite(RenderLayer.ENEMY, "wolfPack.png");
        examineText = "A pack of hungry wolves circle in the room. It seems they've seen you!";
        jsonFile = "json"+System.getProperty("file.separator")+"enemyStats"+System.getProperty("file.separator")+"wolfPack.json";
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a pack of wolves.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You ready your weapon and charge at the wolves!");
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("As you approach the wolves, they snarl and pounce forward!");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("It's a fight!");
                fight();
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You definitely are not holding these wolves right now.");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The wolves aren't locked, so far as you know.");
            }
        }
    }

}
