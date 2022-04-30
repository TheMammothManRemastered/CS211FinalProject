package rootPackage.Battle.Intents;

import rootPackage.Battle.Combatants.Combatant;

/**
 * Parent ADT of all Intents, a simple effect applied by an action.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public abstract class Intent {
    protected Combatant target;
    protected double value;
                //determines how much value will be tinkered with the health of the target
    protected boolean notifyPlayerWhenApplied;

    //constructor
    //Default constructor is basically non-existent
    public Intent(Combatant target, double value, boolean notifyPlayerWhenApplied){
        this.target = target;
        this.value = value;
        this.notifyPlayerWhenApplied = notifyPlayerWhenApplied;
    }



    //Getter methods
    public Combatant getTarget() {
        return this.target;
    }
    public double getValue() {
        return this.value;
    }
    public boolean getNotifyPlayerWhenApplied(){
        return this.notifyPlayerWhenApplied;
    }



    //Setter methods
    public void setNotifyPlayerWhenApplied(boolean notifyPlayerWhenApplied) {
        this.notifyPlayerWhenApplied = notifyPlayerWhenApplied;
    }
    public void setTarget(Combatant target) {
        this.target = target;
    }
    public void setValue(double value) {
        this.value = value;
    }


    //Applying the Action, but this method will prob never be called
    public void apply(Combatant target){
        if(this.notifyPlayerWhenApplied)
            this.notifyPlayer();
    }


    public abstract void notifyPlayer();
                //Got an error saying cannot override final notify of object
                //So renamed it


    //need to come back to when boost defense or any other additions Intents are added
    public static Intent parseToIntent(Combatant target, String name, double value){
        return switch (name) {
            case "DealDamage" -> new DealDamageIntent(target, value, true);
            case "RaiseAttack" -> new RaiseAttackIntent(target, value, true);
            default -> null;
        };
    }


}
