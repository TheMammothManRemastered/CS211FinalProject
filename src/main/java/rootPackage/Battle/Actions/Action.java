package rootPackage.Battle.Actions;

import org.json.simple.parser.ParseException;
import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Main;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the action class
 * Validates an intent and applys a set of intents
 */
public class Action {
    /*
    The reason for this class is the implementation of the modifier
     */

    //Data fields
    private final String name;
    private final ArrayList<Intent> intentsToApply;
    private Combatant target;
    private int usesRemaining;
    private String message;

    //private String message;
    //Message was not needed, since I have set it up such that every time and action is performed the
    //corresponding message is printed

    public Action() {
        this.name = "defaultName";
        this.message = "default";
        this.usesRemaining = 101;
        this.intentsToApply = new ArrayList<>();
    }       //Creating in order to create objects that are not null

    //essentially place holders
    //Constructor
    public Action(String name, Combatant target, int usesRemaining, ArrayList<Intent> intents) throws IOException, ParseException {
        this.name = name;
        this.message = "default";
        this.usesRemaining = usesRemaining;
        this.intentsToApply = intents;
    }


    //getter methods
    public int getUsesRemaining() {
        return usesRemaining;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Intent> getIntentsToApply() {
        return intentsToApply;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean outOfMoves() {
        return usesRemaining < 1;
    }

    //Executes/Performs only action that need to be applied
    //Called during battle for player action and enemy action, which will then call the
    //intent apply function which will perform the action that is needed
    public void execute() {
        if (this.usesRemaining < 100) {   //greater than 100, never ending
            this.usesRemaining--;
        }
        Main.mainWindow.getConsoleWindow().addEntryToHistory(this.message);
        for (Intent intent : intentsToApply) {
            intent.apply(intent.getTarget());
        }
    }

    /**
     * Checks if this action has any {@link Intent Intent}s that do damage, and returns the total damage that will be dealt.
     */
    public int doesDamage() {
        int damage = 0;
        for (Intent intent : intentsToApply) {
            if (intent.getTargetStat().equals("hpStat")) {
                damage += (int) intent.getValue();
            }
        }
        return damage;
    }
}
