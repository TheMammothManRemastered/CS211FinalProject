package rootPackage.Battle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.Battle.AI.AI;
import rootPackage.Battle.AI.AILevel1;
import rootPackage.Battle.AI.AILevel2;
import rootPackage.Battle.AI.AILevel3;
import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.Actions.Intent;
import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Battle.Combatants.Enemy;
import rootPackage.Battle.Combatants.Player;
import rootPackage.Level.Features.Equipment.EquipmentAlias;
import rootPackage.Level.Features.Equipment.EquipmentFeature;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Features.TrapdoorKey;
import rootPackage.Main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * The battle supervisor is where eveything comes together
 * the player and enemy are set up from the respective json files
 * and the battle method is called, to perform the battle with the player and enemy objects
 *
 * @author jovin
 */
public class BattleSupervisor {

    /*
     * I want to have all the action files available,
     * don't know what all of them are though,
     * So need help
     */

    private final String actionAttack = "attack.json";
    //TODO: make these
    private final String actionDefense = "";
    private final String actionReduceAttack = "";
    public boolean battleClosed;
    public Enemy enemy;
    public Player player; // this gets accessed somewhere else

    private Combatant current;
    private String enemyJSONName;

    //private final String run = "";//This run action should be a file without anything in it; all values should be default but they don't do anything
    //Since the intent will do all the work
    /*
    I don't want to make the enemy run,
    I don't think that is a good idea, then the player may not finish the game
    it might be better if he doesn't
     */

    public BattleSupervisor(String EnemyJson) {
        enemyJSONName = EnemyJson;
        try {
            settingUp(EnemyJson);
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }

    public void settingUp(String enemyJson) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        /*
         * Setting up player
         */
        player = Main.player.generateStats();

        /*
         * Setting up the Enemy
         */
        createEnemy(enemyJson, parser);

        /*
         * Enemy is set up
         * Enemy valid actions are set up
         * Player is set up
         */

        /*
         * Setting up player valid actions
         */
        ArrayList<Feature> equipment = Main.player.getPlayerAsFeature().getChildren(FeatureFlag.EQUIPPABLE);
        ArrayList<Action> actions = new ArrayList<>();
        for (Feature feature : equipment) {
            EquipmentFeature equipmentFeature = ((EquipmentFeature) feature);
            JSONArray movesAsJson = equipmentFeature.getActionsJsonField();
            JSONArray descriptions = equipmentFeature.getDescriptionsJsonField();
            for (int i = 0; i < movesAsJson.size(); i++) {
                Object child = movesAsJson.get(i);
                String moveName = (String) child;
                JSONObject moveJson = (JSONObject) parser.parse(new FileReader("json" + System.getProperty("file.separator") + "moves" + System.getProperty("file.separator") + moveName + ".json"));
                Combatant target = ((boolean) moveJson.get("targetSelf")) ? player : enemy;
                int uses = Math.toIntExact((long) moveJson.get("uses"));
                JSONArray intentsJsonArray = (JSONArray) moveJson.get("intents");
                ArrayList<Intent> intentsToApply = new ArrayList<>();
                for (Object intentJson : intentsJsonArray) {
                    String intentName = (String) ((JSONObject) intentJson).get("name");
                    String valueStat = (String) ((JSONObject) intentJson).get("valueStat");
                    String modifier = (String) ((JSONObject) intentJson).get("modifier");
                    String targetStat = (String) ((JSONObject) intentJson).get("targetStat");
                    double value = 0.0;
                    switch (valueStat) {
                        case "hpStat" -> {
                            value = player.getCurrentHp();
                            value = applyModifier(value, modifier);
                        }
                        case "attackStat" -> {
                            value = player.getAttack();
                            value = applyModifier(value, modifier);
                        }
                        case "blockStat" -> {
                            value = player.getBlock();
                            value = applyModifier(value, modifier);
                        }
                    }
                    Intent intent = new Intent(target, value, intentName.equals("dealDamage"), targetStat);
                    intentsToApply.add(intent);
                }
                Action action = new Action(moveName, target, uses, intentsToApply);
                action.setMessage((String) descriptions.get(i));
                actions.add(action);
            }
        }
        player.setAvailableActions(actions);

    }

    public void battle(Combatant player, Combatant enemy) {

        JSONParser parser = new JSONParser();

        current = enemy;
        if (player.getPriority() >= enemy.getPriority()) {
            current = player;
        }

        boolean run = false; //flag variable for the run fuction

        while (!run) {
            // update player's and enemy's actions so the numbers add up properly
            boolean currentIsPlayer = current instanceof Player;
            if (currentIsPlayer) {
                try {
                    updatePlayer(parser);
                } catch (IOException | ParseException exception) {
                    exception.printStackTrace();
                }
            } else {
                try {
                    updateEnemy(parser);
                    if (!(((Enemy) enemy).getEnemyAI() instanceof AILevel1 || ((Enemy) enemy).getEnemyAI() instanceof AILevel2)) {
                        updatePlayer(parser);
                    }
                } catch (IOException | ParseException exception) {
                    exception.printStackTrace();
                }
            }

            Action selectedMove = null;
            Main.battleManagerMain.readyInput();
            selectedMove = current.askForInput();
            if (battleClosed) {
                break;
            }
            selectedMove.execute();
            // swap active player
            if (currentIsPlayer) {
                current = enemy;
            } else {
                current = player;
            }
            // is the battle over?
            if (enemy.getCurrentHp() <= 0) {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The enemy died, YOU WIN");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("-*-*-*-*-*-*-");
                Main.player.setCurrentHP(player.getCurrentHp());
                Main.player.getCurrentRoom().getRoomAsFeature().getChildren(FeatureFlag.ENEMY).get(0).removeFromPlay();
                Main.mainWindow.getViewportPanel().drawRoom(Main.player.getCurrentRoom().getRoomAsFeature());
                Main.player.addGold(Main.currentBattle.enemy.getGold());
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You gain %d gold from your defeated foe!".formatted(Main.currentBattle.enemy.getGold()));
                String[] itemDropNames = Main.currentBattle.enemy.getDropFeatures();
                for (String name : itemDropNames) {
                    if (name.equals("trapdoorKey")) {
                        Main.player.getPlayerAsFeature().addChild(new TrapdoorKey());
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You feel something strange on your belt... You look down and see the trapdoor key has appeared!");
                        continue;
                    }
                    EquipmentFeature ef = EquipmentAlias.getEquipment(name);
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("A %s drops to the floor of the room!".formatted(ef.getPrimaryName()));
                    Main.player.getCurrentRoom().getRoomAsFeature().addChild(ef);
                }
                Main.currentBattle = null;
                break;
            } else if (player.getCurrentHp() <= 0) {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You died, GAME OVER");
                Main.mainWindow.getConsoleWindow().addEntryToHistory("-*-*-*-*-*-*-");
                Main.currentBattle = null;
                Main.mainWindow.getConsoleWindow().addEntryToHistory("This is the end of the line for your adventure, and the game will close in 7 seconds. Restart the game to try again.");
                try {
                    Main.gameOver = true;
                    TimeUnit.SECONDS.sleep(7);
                    System.exit(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }


    }

    private void createEnemy(String json, JSONParser parser) throws IOException, ParseException {
        JSONObject jsonEnemy = (JSONObject) parser.parse(new FileReader(json));
        int level = Math.toIntExact((long) jsonEnemy.get("intelligence"));
        AI ai = null;
        if (level == 1)
            ai = new AILevel1();
        else if (level == 2)
            ai = new AILevel2();
        else
            ai = new AILevel3();

        int hp = Math.toIntExact((long) jsonEnemy.get("health"));
        int attack = Math.toIntExact((long) jsonEnemy.get("baseAttack"));
        double block = (double) jsonEnemy.get("baseAbsorption");
        int priority = Math.toIntExact((long) jsonEnemy.get("priority"));
        int gold = Math.toIntExact((long) jsonEnemy.get("goldDrop"));
        JSONArray itemDrops = (JSONArray) jsonEnemy.get("itemDrop");
        String[] itemNames = new String[itemDrops.size()];
        for (int i = 0; i < itemDrops.size(); i++) {
            Object item = itemDrops.get(i);
            itemNames[i] = (String) item;
        }

        Enemy output = new Enemy(hp, hp, attack, block, priority, ai);
        output.setGold(gold);
        output.setDropFeatures(itemNames);
        this.enemy = output;

        JSONArray movesAsJson = (JSONArray) jsonEnemy.get("possibleMoves");
        JSONArray moveDescriptionsAsJson = (JSONArray) jsonEnemy.get("moveDescriptions");
        ArrayList<Action> possibleActions = new ArrayList<>();
        for (Object child : movesAsJson) {
            String moveName = (String) child;
            JSONObject moveJson = (JSONObject) parser.parse(new FileReader("json" + System.getProperty("file.separator") + "moves" + System.getProperty("file.separator") + moveName + ".json"));
            Combatant target = ((boolean) moveJson.get("targetSelf")) ? enemy : player;
            int uses = Math.toIntExact((long) moveJson.get("uses"));
            JSONArray intentsJsonArray = (JSONArray) moveJson.get("intents");
            ArrayList<Intent> intentsToApply = new ArrayList<>();
            for (Object intentJson : intentsJsonArray) {
                String intentName = (String) ((JSONObject) intentJson).get("name");
                String valueStat = (String) ((JSONObject) intentJson).get("valueStat");
                String modifier = (String) ((JSONObject) intentJson).get("modifier");
                String targetStat = (String) ((JSONObject) intentJson).get("targetStat");
                double value = 0.0;
                switch (valueStat) {
                    case "hpStat" -> {
                        value = enemy.getCurrentHp();
                        value = applyModifier(value, modifier);
                    }
                    case "attackStat" -> {
                        value = enemy.getAttack();
                        value = applyModifier(value, modifier);
                    }
                    case "blockStat" -> {
                        value = enemy.getBlock();
                        value = applyModifier(value, modifier);
                    }
                }
                Intent intent = new Intent(target, value, intentName.equals("dealDamage"), targetStat);
                intentsToApply.add(intent);
            }

            possibleActions.add(new Action(moveName, target, uses, intentsToApply));
        }
        for (int i = 0; i < possibleActions.size(); i++) {
            Action action = possibleActions.get(i);
            action.setMessage((String) moveDescriptionsAsJson.get(i));
        }
        enemy.setAvailableActions(possibleActions);
    }

    private double applyModifier(double value, String modifier) {
        if ("none".equals(modifier)) {
            return value;
        } else if (modifier.contains("+") || modifier.contains("-")) {
            if (modifier.contains("%")) {
                modifier = modifier.replace("%", "");
                value *= 1.0 + (Double.parseDouble(modifier) / 100.0);
            } else {
                value += Double.parseDouble(modifier);
            }
        }
        return value;
    }

    private void updatePlayer(JSONParser parser) throws IOException, ParseException {
        ArrayList<Feature> equipment = Main.player.getPlayerAsFeature().getChildren(FeatureFlag.EQUIPPABLE);
        ArrayList<Action> actions = new ArrayList<>(player.getValidActions());
        actions.removeIf(Action::outOfMoves);
        for (Feature feature : equipment) {
            EquipmentFeature equipmentFeature = ((EquipmentFeature) feature);
            JSONArray movesAsJson = equipmentFeature.getActionsJsonField();
            JSONArray descriptions = equipmentFeature.getDescriptionsJsonField();
            for (int i = 0; i < movesAsJson.size(); i++) {
                Object child = movesAsJson.get(i);
                String moveName = (String) child;
                JSONObject moveJson = (JSONObject) parser.parse(new FileReader("json" + System.getProperty("file.separator") + "moves" + System.getProperty("file.separator") + moveName + ".json"));
                Combatant target = ((boolean) moveJson.get("targetSelf")) ? player : enemy;
                JSONArray intentsJsonArray = (JSONArray) moveJson.get("intents");
                ArrayList<Intent> intentsToApply = new ArrayList<>();
                for (Object intentJson : intentsJsonArray) {
                    String intentName = (String) ((JSONObject) intentJson).get("name");
                    String valueStat = (String) ((JSONObject) intentJson).get("valueStat");
                    String modifier = (String) ((JSONObject) intentJson).get("modifier");
                    String targetStat = (String) ((JSONObject) intentJson).get("targetStat");
                    double value = 0.0;
                    switch (valueStat) {
                        case "hpStat" -> {
                            value = player.getCurrentHp();
                            value = applyModifier(value, modifier);
                        }
                        case "attackStat" -> {
                            value = player.getAttack();
                            value = applyModifier(value, modifier);
                        }
                        case "blockStat" -> {
                            value = player.getBlock();
                            value = applyModifier(value, modifier);
                        }
                    }
                    Intent intent = new Intent(target, value, intentName.equals("dealDamage"), targetStat);
                    intentsToApply.add(intent);
                }
                int uses = 101;
                for (int j = 0; j < actions.size(); j++) {
                    Action action = actions.get(j);
                    if (action.getName().equals(moveName)) {
                        uses = action.getUsesRemaining();
                        actions.remove(action);
                    }
                }
                Action action = new Action(moveName, target, uses, intentsToApply);
                action.setMessage((String) descriptions.get(i));
                actions.add(action);
            }
        }
        player.setAvailableActions(actions);
    }

    private void updateEnemy(JSONParser parser) throws IOException, ParseException {
        JSONObject jsonEnemy = (JSONObject) parser.parse(new FileReader(enemyJSONName));

        JSONArray movesAsJson = (JSONArray) jsonEnemy.get("possibleMoves");
        JSONArray moveDescriptionsAsJson = (JSONArray) jsonEnemy.get("moveDescriptions");
        ArrayList<Action> possibleActions = new ArrayList<>(enemy.getValidActions());
        possibleActions.removeIf(Action::outOfMoves);
        for (Object child : movesAsJson) {
            String moveName = (String) child;
            JSONObject moveJson = (JSONObject) parser.parse(new FileReader("json" + System.getProperty("file.separator") + "moves" + System.getProperty("file.separator") + moveName + ".json"));
            Combatant target = ((boolean) moveJson.get("targetSelf")) ? enemy : player;
            JSONArray intentsJsonArray = (JSONArray) moveJson.get("intents");
            ArrayList<Intent> intentsToApply = new ArrayList<>();
            for (Object intentJson : intentsJsonArray) {
                String intentName = (String) ((JSONObject) intentJson).get("name");
                String valueStat = (String) ((JSONObject) intentJson).get("valueStat");
                String modifier = (String) ((JSONObject) intentJson).get("modifier");
                String targetStat = (String) ((JSONObject) intentJson).get("targetStat");
                double value = 0.0;
                switch (valueStat) {
                    case "hpStat" -> {
                        value = enemy.getCurrentHp();
                        value = applyModifier(value, modifier);
                    }
                    case "attackStat" -> {
                        value = enemy.getAttack();
                        value = applyModifier(value, modifier);
                    }
                    case "blockStat" -> {
                        value = enemy.getBlock();
                        value = applyModifier(value, modifier);
                    }
                }
                Intent intent = new Intent(target, value, intentName.equals("dealDamage"), targetStat);
                intentsToApply.add(intent);
            }

            int uses = 101;
            for (int j = 0; j < possibleActions.size(); j++) {
                Action action = possibleActions.get(j);
                if (action.getName().equals(moveName)) {
                    uses = action.getUsesRemaining();
                    possibleActions.remove(action);
                }
            }

            possibleActions.add(new Action(moveName, target, uses, intentsToApply));
        }
        for (int i = 0; i < possibleActions.size(); i++) {
            Action action = possibleActions.get(i);
            action.setMessage((String) moveDescriptionsAsJson.get(i));
        }
        enemy.setAvailableActions(possibleActions);
    }
}
