package rootPackage.Battle.StatusEffects;

import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Battle.Intents.Intent;

/**
 * Don't know what the use is,
 * This is never used
 *
 */
public class StatusEffects {
    private String name;
    private int turnsRemaining = 1;
    private Intent intent;


    //Constructors

    /*public StatusEffects(){

    }*/
    //Default doesn't seem necessary
    //since by default the player will have no effect
    public StatusEffects(String name, int turnsRemaining, Intent intent){
        this.name = name;
        this.turnsRemaining = turnsRemaining;
        this.intent = intent;
    }



    //Doing getter for everything jsut to be on safer side
    public String name(){
        return this.name;
    }
    public int getTurnsRemaining() {
        return turnsRemaining;
    }
    public Intent getIntent() {
        return intent;
    }



    //Setter methods
    public void setIntent(Intent intent) {
        this.intent = intent;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTurnsRemaining(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }



    //Don't know when this is used
    public boolean applyStatusEffect(Combatant target) {
        if (this.turnsRemaining > 0) {
            turnsRemaining--;

            intent.apply(target);   //callling the particular intent to apply the status effect
                                    //Poison -> dealDamage
            return true;
        }
        return false;
    }



}
