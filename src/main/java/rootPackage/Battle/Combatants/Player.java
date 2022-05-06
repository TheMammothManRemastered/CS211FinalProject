package rootPackage.Battle.Combatants;

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

    // Will here, I wrote this bit. this is DEFINITELY not how threads are supposed to be used, look into that
    public Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("lego city");
            }
        }
    };

    //Constructors
    public Player(){
        this(500,450,60,0.2,2);
    }
    public Player(int maxHp, int currentHp, int attack, double block, int priority){
        super(maxHp,currentHp,attack,block,priority);
    }

    public int getChosenAction() {
        return chosenAction;
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
            System.out.println("waiting");
            wait();
            System.out.println("finished waiting");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(chosenAction);
        if (chosenAction < 0 || chosenAction >= availableActions.size()) {
            Main.mainWindow.getConsoleWindow().addEntryToHistory("Using %s".formatted(availableActions.get(0).getName()));
            return availableActions.get(0);
        }
        return availableActions.get(chosenAction);
    }

    public synchronized void wakeUp() {
        System.out.println("wake up call");
        notifyAll();
    }

}
