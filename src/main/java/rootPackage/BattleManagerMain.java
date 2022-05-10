package rootPackage;

import rootPackage.Battle.BattleSupervisor;

/**
 * A {@link Thread Thread} to manage an ongoing battle.
 *
 * @author William Owens
 * @version 1.1
 */
public class BattleManagerMain extends Thread {

    /**
     * Starts a {@link BattleSupervisor BattleSupervisor} for a given enemy JSON file in its own thread.
     *
     * @param enemyJson The path for the enemy's JSON file.
     */
    public void go(String enemyJson) {
        Main.currentBattle = new BattleSupervisor(enemyJson);
        new Thread(() -> {
            System.out.println("running the battle thread");
            Main.currentBattle.battle(Main.currentBattle.player, Main.currentBattle.enemy);
            System.out.println("is the thread still alive?");
        }).start();
    }

    /**
     * Starts a thread that waits until the player gives a valid battle input, then notifies the BattleManager to proceed.
     */
    public void readyInput() {
        new Thread(() -> {
            StringBuilder holde = new StringBuilder();
            while (!Main.mainWindow.getConsoleWindow().playerGivenBattleInput) {
                holde.append(Main.mainWindow.getConsoleWindow().playerGivenBattleInput);
                holde.setLength(0);
                // I don't know why, I don't want to know why, I shouldn't have to wonder why but for whatever reason
                // this stupid field isn't being read correctly unless we do this terribleness
            }
            Main.mainWindow.getConsoleWindow().playerGivenBattleInput = false;
            Main.currentBattle.player.wakeUp();
        }).start();
    }
}
