package rootPackage;

import rootPackage.Battle.BattleSupervisor;

public class BattleManagerMain extends Thread {
    public void go(String enemyJson) {
        Main.currentBattle = new BattleSupervisor(enemyJson, (rootPackage.Battle.Combatants.Player) Main.player.generateStats());
        new Thread() {
            @Override
            public void run() {
                System.out.println("running the battle thread");
                Main.currentBattle.battle(Main.currentBattle.player, Main.currentBattle.enemy);
                System.out.println("is the thread still alive?");
            }
        }.start();
    }

    public void readyInput() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("running the input thread");
                StringBuffer holde = new StringBuffer();
                while (!Main.mainWindow.getConsoleWindow().playerGivenBattleInput) {
                    holde.append(Main.mainWindow.getConsoleWindow().playerGivenBattleInput);
                    holde.setLength(0);
                    //horrid, this is done so playerGivenBattleInput is "updated" properly
                }
                System.out.println("player put a value in");
                Main.mainWindow.getConsoleWindow().playerGivenBattleInput = false;
                System.out.println(Main.mainWindow.getConsoleWindow().playerGivenBattleInput);
                Main.currentBattle.player.wakeUp();
            }
        }.start();
    }
}
