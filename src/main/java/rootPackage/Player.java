package rootPackage;

import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.Combatants.Combatant;
import rootPackage.Level.Features.Equipment.*;
import rootPackage.Level.Features.Equipment.AccessoryEffects.AccessoryEffect;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.Features.Meat;
import rootPackage.Level.Features.Features.TrapdoorKey;
import rootPackage.Level.Features.TopLevel.PlayerFeature;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;

import java.util.ArrayList;

/**
 * Represents the player. Currently only holds the player's location.
 *
 * @author William Owens
 * @version 0.2
 */
public class Player {

    private RoomNode currentRoom;
    private PlayerFeature playerAsFeature;
    private int currentHP;
    private Combatant currentEnemy;
    private int gold;

    public PlayerFeature getPlayerAsFeature() {
        return playerAsFeature;
    }

    public Player() {
        this(null);
    }

    public Player(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
        playerAsFeature = new PlayerFeature("You", new String[]{"self", "player", "yourself"});
        WeaponFeature weaponFeature = (WeaponFeature) EquipmentAlias.getEquipment("steelSword");
        ArmorFeature armorFeature = (ArmorFeature) EquipmentAlias.getEquipment("steelArmor");
        ShieldFeature shieldFeature = (ShieldFeature) EquipmentAlias.getEquipment("steelShield");
        playerAsFeature.addChild(weaponFeature);
        playerAsFeature.addChild(armorFeature);
        playerAsFeature.addChild(shieldFeature);
        //debug
        playerAsFeature.addChild(new TrapdoorKey());
        playerAsFeature.addChild(new Meat());
        currentHP = 150;
        gold = 0;
    }

    public RoomNode getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public Combatant generateStats() {
        // first, get all the equipment features and put them in a list
        ArrayList<Feature> equipment = playerAsFeature.getChildren(FeatureFlag.EQUIPPABLE);
        int maxHP = 0;
        int attack = 60;
        double block = 0.2;
        int priority = 2;
        for (Feature feature : equipment) {
            if (feature instanceof ArmorFeature) {
                maxHP = (int) ((ArmorFeature) feature).getValue();
            }
            if (feature instanceof WeaponFeature) {
                attack = (int) ((WeaponFeature) feature).getValue();
            }
            if (feature instanceof ShieldFeature) {
                block = ((ShieldFeature) feature).getValue();
            }
            if (feature instanceof AccessoryFeature) {
                AccessoryFeature accessoryFeature = (AccessoryFeature) feature;
                ArrayList<AccessoryEffect> effects = accessoryFeature.getEffects();
                for (AccessoryEffect effect : effects) {
                    switch (effect.getAffectedStat()) {
                        case "priorityStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    priority = (int) effect.getValue();
                                }
                                case ADD -> {
                                    priority += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    priority *= 1.0 + effect.getValue();
                                }
                            }
                        }
                        case "hpStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    maxHP = (int) effect.getValue();
                                }
                                case ADD -> {
                                    maxHP += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    maxHP *= 1.0 + effect.getValue();
                                }
                            }

                        }
                        case "blockStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    block = (int) effect.getValue();
                                }
                                case ADD -> {
                                    block += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    block *= 1.0 + effect.getValue();
                                }
                            }

                        }
                        case "attackStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    attack = (int) effect.getValue();
                                }
                                case ADD -> {
                                    attack += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    attack *= 1.0 + effect.getValue();
                                }
                            }

                        }
                    }
                }
            }
        }

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
        return new rootPackage.Battle.Combatants.Player(maxHP, currentHP, attack, block, priority);
    }

    public void printStatsToConsole() {
        // first, get all the equipment features and put them in a list
        ArrayList<Feature> equipment = playerAsFeature.getChildren(FeatureFlag.EQUIPPABLE);
        int maxHP = 0;
        int attack = 60;
        double block = 0.2;
        int priority = 2;
        for (Feature feature : equipment) {
            if (feature instanceof ArmorFeature) {
                maxHP = (int) ((ArmorFeature) feature).getValue();
            }
            if (feature instanceof WeaponFeature) {
                attack = (int) ((WeaponFeature) feature).getValue();
            }
            if (feature instanceof ShieldFeature) {
                block = ((ShieldFeature) feature).getValue();
            }
            if (feature instanceof AccessoryFeature) {
                AccessoryFeature accessoryFeature = (AccessoryFeature) feature;
                ArrayList<AccessoryEffect> effects = accessoryFeature.getEffects();
                for (AccessoryEffect effect : effects) {
                    switch (effect.getAffectedStat()) {
                        case "priorityStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    priority = (int) effect.getValue();
                                }
                                case ADD -> {
                                    priority += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    priority *= 1.0 + effect.getValue();
                                }
                            }
                        }
                        case "hpStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    maxHP = (int) effect.getValue();
                                }
                                case ADD -> {
                                    maxHP += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    maxHP *= 1.0 + effect.getValue();
                                }
                            }

                        }
                        case "blockStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    block = (int) effect.getValue();
                                }
                                case ADD -> {
                                    block += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    block *= 1.0 + effect.getValue();
                                }
                            }

                        }
                        case "attackStat" -> {
                            switch (effect.getMode()) {
                                case SET -> {
                                    attack = (int) effect.getValue();
                                }
                                case ADD -> {
                                    attack += effect.getValue();
                                }
                                case ADD_PERCENT -> {
                                    attack *= 1.0 + effect.getValue();
                                }
                            }

                        }
                    }
                }
            }
        }

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }

        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current HP is %d.".formatted(currentHP));
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current max HP is %d.".formatted(maxHP));
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current damage is %d.".formatted(attack));
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current damage absorption is %.2f%%.".formatted(block*100));
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current priority, or speed, is %d.".formatted(priority));
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }
}
