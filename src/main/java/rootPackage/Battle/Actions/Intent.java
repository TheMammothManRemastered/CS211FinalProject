package rootPackage.Battle.Actions;

import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Main;

/**
 * This s the intent class
 * This determines what effect to do to the enemy/player
 * And also determines how it is applied
 *
 * @author jovin
 */
public class Intent {
    private final String targetStat;
    protected Combatant target;
    protected double value;
    //determines how much value will be tinkered with the health of the target
    protected boolean notifyPlayerWhenApplied;

    //constructor
    //Default constructor is basically non-existent
    public Intent(Combatant target, double value, boolean notifyPlayerWhenApplied, String targetStat) {
        this.target = target;
        this.value = value;
        this.notifyPlayerWhenApplied = notifyPlayerWhenApplied;
        this.targetStat = targetStat;
    }


    //Getter methods
    public Combatant getTarget() {
        return this.target;
    }

    public double getValue() {
        return this.value;
    }

    public boolean getNotifyPlayerWhenApplied() {
        return this.notifyPlayerWhenApplied;
    }

    public String getTargetStat() {
        return targetStat;
    }

    //Applying the Action, but this method will prob never be called
    public void apply(Combatant target) {
        switch (targetStat) {
            case "hpStat" -> {
                System.out.println("damage number pre modification: %f".formatted(value));
                target.takeDamage(value);
            }
            case "attackStat" -> {
                target.setAttack((int) value);
            }
            case "blockStat" -> {
                if (value > 1) {
                    target.setBlock(1.0);
                }
                else {
                    target.setBlock(value);
                }
            }
        }
        if (notifyPlayerWhenApplied) {
            notifyPlayer();
        }
    }


    public void notifyPlayer() {
        Main.mainWindow.getConsoleWindow().addEntryToHistory(((target instanceof rootPackage.Battle.Combatants.Player) ? "Your " : "The foe's ") + "HP is now " + target.getCurrentHp());
    }
    //Got an error saying cannot override final notify of object
    //So renamed it

}
