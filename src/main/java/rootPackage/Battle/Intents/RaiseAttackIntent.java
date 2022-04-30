package rootPackage.Battle.Intents;

import rootPackage.Battle.Combatants.Combatant;

/**
 * An intent to raise the attack stat of a target.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public class RaiseAttackIntent extends Intent{

    /*
    Required me to do this, Context: not in pseudo code
     */
    public RaiseAttackIntent(Combatant target, double value, boolean notifyPlayerWhenApplied) {
        super(target, value, notifyPlayerWhenApplied);
    }


    //Applys the raise attack intent
    @Override
    public void apply(Combatant target) {
        target.setAttack((int) this.value);

        if (target.getAttack() < 1) {
            target.setAttack(1);
            // we don't want neither side to be able to do damage by default
        }
        this.notifyPlayer();    //called to print message when the attack is raised
    }


    //Sending an output message stating that attack was raised
    @Override
    public void notifyPlayer() {
        System.out.println("Attack raise");
        //interface.display("Attack raise");
    }
}
