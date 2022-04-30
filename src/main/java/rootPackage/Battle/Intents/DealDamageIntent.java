package rootPackage.Battle.Intents;

import rootPackage.Battle.Combatants.Combatant;

/**
 * An intent to deal damage to a target.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public class DealDamageIntent extends Intent{
    //Constructor
    public DealDamageIntent(Combatant target, double value, boolean notifyPlayerWhenApplied) {
        super(target, value, notifyPlayerWhenApplied);
        this.value -= this.value * target.getBlock();
    }


    /**
     *I get that you will deal damage by reducing from the block
     * but, what happens when the damage is poison,
     * isn't it "true damage" as in,
     * isn't it supposed to directly affect the player/enemy
     */
    @Override
    public void apply(Combatant target) {
        target.setCurrentHp(target.getCurrentHp() - (int) this.value);

        if (target.getCurrentHp() > target.getMaxHp()) {
            target.setCurrentHp(target.getMaxHp());
        }
        else if (target.getCurrentHp() < 0) {
            target.setCurrentHp(0);
        }
    }

    /**
     * Need to ask what the interface.display() is
     * I think it displays stuff to the screen but it is giving an error
     */
    @Override
    public void notifyPlayer() {
        String display = "";
        if (value > 0) {
            display = "Dealt "+this.value+" damage! Ouch, that looks like it hurt!";
            //interface.display("Dealt {value} damage! Ouch, that looks like it hurt!);
        }
        if (value <= 0) {
            display = "No damage was sustained";
            //interface.display("No damage was sustained!");
        }
        System.out.println(display);
    }
}
