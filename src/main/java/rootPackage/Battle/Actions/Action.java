package rootPackage.Battle.Actions;

import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Battle.Combatants.Enemy;
import rootPackage.Battle.Intents.Intent;
import java.io.FileReader;
import java.util.List;

/**
 * A class representing an action that a {@link Combatant Combatant} may take in battle.
 *
 * @author Jovin Antony-Maria
 * @version 1.0
 */
public class Action {
    //Data fields
    private List<Intent> intentsToApply;
    private String message;
    private int usesRemaining;


    //Constructor
    public Action(Combatant user,String actionJson, String message, int usesRemaining, List<Intent> intents) throws IOException, ParseException {
        this.message = message;
        this.usesRemaining = usesRemaining;

        /**
         * Here I am assuming that the actionJson is the file name for a json
         * Not really sure, and if I am interpreting wrong let me know
         */
        JSONParser parser = new JSONParser();
        JSONObject jsonFile = (JSONObject) parser.parse(new FileReader(actionJson));
        Combatant target =  ((boolean) jsonFile.get("targetSelf")) ? user: new Enemy();
                            /**
                            Don't exactly know how this works,
                             */

        for(Intent entry: intents){  //I think by intents u meant intents to apply, which I got as a parameter
                                    //which I think you forgot, but it could also be in the json file, so not sure
            double value = 0.0;
            value =  (double) jsonFile.get("valueStat");
            String modifier = (String) jsonFile.get("modifier");
            /**
             * Don't really know what x is
             * Assuming that it is a variable in the json file
             */

            double x = (double) jsonFile.get("x");
            switch(modifier){
                //case "none":
                    //break;
                //none really doesn't do anything so sending it to default, which is the same
                case "x":
                    value = x;
                    break;  //Doing nothing since assuming that x is value stat
                case "%x":
                    value = value * (x/100.0);
                    break;
                case "+x":
                    value += x;
                    break;
                case "-x":
                    value -= x;
                    break;
                case "+%x":
                    value = value + (value * ( x / 100.0));
                    break;
                case "-%x":
                    value  = value - (value * ( x/ 100.0));
                    break;
                default:
                    break;
            }
            intentsToApply.add(Intent.parseToIntent(target,(String) jsonFile.get("name"), value));
        }
    }

    //getter methods
    public int getUsesRemaining() {
        return usesRemaining;
    }
    public List<Intent> getIntentsToApply() {
        return intentsToApply;
    }
    public String getMessage() {
        return message;
    }




    //Setter methods
    public void setIntentsToApply(List<Intent> intentsToApply) {
        this.intentsToApply = intentsToApply;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setUsesRemaining(int usesRemaining) {
        this.usesRemaining = usesRemaining;
    }





    //Executes/Performs all the actions that need to be applied
    public void execute(){
        if(this.usesRemaining < 100){   //greater than 100, never ending
            usesRemaining--;
        }
        //interface.display(this.message);
        System.out.println(this.message);

        for(Intent intent: this.intentsToApply){
            intent.apply(intent.getTarget());
        }
    }
}
