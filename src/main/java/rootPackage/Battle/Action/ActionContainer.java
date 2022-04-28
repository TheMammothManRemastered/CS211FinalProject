package rootPackage.Battle.Action;

import rootPackage.Battle.Action.Intents.Intent;
import rootPackage.Main;

public class ActionContainer {

    private String messageToPrint;
    private Intent intent;

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    /**
     * Executes the intent in this container, and sends the messageToPrint to the console.
     */
    public void execute() {
        //TODO: do some stuff with the intent

        // send message to print to the console's history
        Main.mainWindow.getConsoleWindow().addEntryToHistory(messageToPrint);
    }


}
