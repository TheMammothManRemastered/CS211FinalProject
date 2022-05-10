package rootPackage;

import rootPackage.Level.Features.Equipment.AccessoryEffects.AccessoryEffect;
import rootPackage.Level.Features.Equipment.*;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Level.Features.TopLevel.PlayerFeature;
import rootPackage.Level.FloorGeneration.Layout.RoomNode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A class representing the player.
 * Stores current player HP between battles, as well as their gold, current location on the map in terms of a {@link rootPackage.Level.FloorGeneration.Layout.RoomNode}, and their associated {@link rootPackage.Level.Features.TopLevel.PlayerFeature PlayerFeature} object.
 * <p></p>
 * NOTE: This class should not be confused with {@link rootPackage.Level.Features.TopLevel.PlayerFeature PlayerFeature}, which is used primarily to manage player inventory and stat modifiers, or {@link rootPackage.Battle.Combatants.Player Player}, which is a {@link rootPackage.Battle.Combatants.Combatant Combatant} to be used in the battle system.
 *
 * @author William Owens
 * @version 1.3
 */
public class Player {

    private final PlayerFeature playerAsFeature;
    private RoomNode currentRoom;
    private int currentHP;
    private int gold;

    public Player(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
        playerAsFeature = new PlayerFeature("You", new String[]{"self", "player", "yourself"});
        currentHP = 225; // obv needs balancing, 225 seems a reasonable starting value for the labyrinth
        setup();
    }

    /**
     * Sets up player's starting equipment.
     */
    private void setup() {
        // default loadout is steel sword, shield and armor, and no gold
        // objects requireNonNull isn't required here, we know these inputs will always work, but it stops the IDE from yelling and it can't really hurt either
        playerAsFeature.addChild(Objects.requireNonNull(EquipmentAlias.getEquipment("steelSword")));
        playerAsFeature.addChild(Objects.requireNonNull(EquipmentAlias.getEquipment("steelArmor")));
        playerAsFeature.addChild(Objects.requireNonNull(EquipmentAlias.getEquipment("steelShield")));
        gold = 0;
    }

    // getters

    public RoomNode getCurrentRoom() {
        return currentRoom;
    }

    public PlayerFeature getPlayerAsFeature() {
        return playerAsFeature;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getGold() {
        return gold;
    }

    // setters

    public void setCurrentRoom(RoomNode currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    // methods

    /**
     * Creates a {@link rootPackage.Battle.Combatants.Player Player Combatant} based on the player's current equipment and stats.
     */
    public rootPackage.Battle.Combatants.Player generateStats() {
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

    /**
     * Gets the player's current stats and prints them to the current {@link rootPackage.Graphics.GUI.ConsoleWindow ConsoleWindow}.
     */
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
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current damage absorption is %.2f%%.".formatted(block * 100));
        Main.mainWindow.getConsoleWindow().addEntryToHistory("Your current priority, or speed, is %d.".formatted(priority));
    }

    /**
     * Checks if the player can afford something with a given price.
     */
    public boolean canAfford(int price) {
        return price <= gold;
    }

    /**
     * Adds a given amount to the player's gold (or removes it, if the parameter is negative). Will never make the player's gold negative.
     *
     * @param gold Integer amount of gold to give the player.
     */
    public void addGold(int gold) {
        this.gold += gold;
        if (this.gold < 0) {
            this.gold = 0;
        }
    }
}
