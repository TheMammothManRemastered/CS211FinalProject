package rootPackage.Battle.Combatants;

import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.StatusEffects.StatusEffects;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a combatant in battle, either a {@link Player Player} or an {@link Enemy Enemy}.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public abstract class Combatant {



    //Data fields
    protected int maxHp, currentHp, attack, priority;
    protected double block;
    protected List<StatusEffects> statusEffects;
    protected List<Action> availableActions;




    //Constructors
    public Combatant(){
        this(500,450,60,0.2,2);
    }
    public Combatant(int maxHp, int currentHp, int attack, double block, int priority){
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.attack = attack;
        this.block = block;
        this.priority = priority;
    }




    //Getter methods
    public int getAttack() {
        return this.attack;
    }
    public int getMaxHp(){
        return this.maxHp;
    }
    public int getCurrentHp(){
        return this.currentHp;
    }
    public int getPriority(){
        return this.priority;
    }
    public double getBlock(){
        return this.block;
    }
    public List<StatusEffects> getStatusEffects(){
        return this.statusEffects;
    }
    public List<Action> getAvailableActions() {
        return this.availableActions;
    }




    //Setter methods
    public void setBlock(double block){
        this.block = block;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public void setStatusEffects(List<StatusEffects> statusEffects){
        this.statusEffects = statusEffects;
    }
    public void setAvailableActions(List<Action> availableActions){
        this.availableActions = availableActions;
    }



    //Returns a list of all the actions that are valid, from all available actions
    //Don't know how they are determined as valid
    public List<Action> getValidActions() {
        List<Action> validActions = new ArrayList<Action>();
        for (Action action: this.availableActions){
            if (action.getUsesRemaining() <= 0) {    /**How is it valid when there are no uses available*/
                validActions.add(action);
            }
        }
        return validActions;
    }


    //public abstract Action askingForInput(List<Actions> validActions);
                    //Call it with getValidActions()

    public void applyStatusEffects() {
        for (StatusEffects effect: statusEffects) {
            effect.applyStatusEffect(this); //this refers to the object calling the method
                                            //Due to polymorphism can be player or enemy
        }
    }



}
