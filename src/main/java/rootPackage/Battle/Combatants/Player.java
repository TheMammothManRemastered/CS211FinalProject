package rootPackage.Battle.Combatants;

import org.json.simple.*;
import rootPackage.Battle.Actions.Action;
import rootPackage.Main;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

/**
 * The player class represents the user
 * The player stat is set during the battle phase by battle supervisor
 * @author jovin
 * @author William Owens
 */
public class Player extends Combatant {

    private int chosenAction;

    //Constructors
    public Player(){
        this(500,450,60,0.2,2);
    }
    public Player(int maxHp, int currentHp, int attack, double block, int priority){
        super(maxHp,currentHp,attack,block,priority);
    }

    public void setChosenAction(int chosenAction) {
        this.chosenAction = chosenAction;
    }

    @Override
    public synchronized Action askForInput() {
        Main.mainWindow.getConsoleWindow().addEntryToHistory("What do you do, (type the number)");
        for (int i = 0; i < availableActions.size(); i++) {
            Action action = availableActions.get(i);
            Main.mainWindow.getConsoleWindow().addEntryToHistory("%d) %s".formatted(i, action.getName()));
        }
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (chosenAction < 0 || chosenAction >= availableActions.size()) {
            Main.mainWindow.getConsoleWindow().addEntryToHistory("Using %s".formatted(availableActions.get(0).getName()));
            return availableActions.get(0);
        }
        return availableActions.get(chosenAction);
    }

    // this probably shouldn't be here, but it makes sense for readability
    public synchronized void wakeUp() {
        notifyAll();
    }

}
