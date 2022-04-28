package rootPackage.Level.Features.Concrete.Equipment;

import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

public class ArmorFeature extends Feature {

    public ArmorFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        flags.add(FeatureFlag.ARMOR);
        flags.add(FeatureFlag.EQUIPABLE);
    }

    private void drop(PlayerAction playerAction) {
        this.setParent(playerAction.getPlayer().getCurrentRoom().getRoomAsFeature());
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP -> {
                ArrayList<Feature> allPlayerChildren = this.getParent().getChildren();
                ArrayList<Feature> armorFeatures = this.getParent().getChildren(FeatureFlag.ARMOR);
                if (armorFeatures.size() != 0) {
                    // the player has no armor equipped, proceed normally, otherwise it has to be doffed
                    for (Feature armor : armorFeatures) {
                        Main.mainWindow.getConsoleWindow().addEntryToHistory("You doff your %s and place it carefully on the ground.".formatted(armor.getPrimaryName()));
                        ((ArmorFeature) armor).drop(playerAction);
                    }
                }
                this.getParent().removeChild(this);
                this.react(PlayerAction.EQUIP);
            }
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("placeholder examine text, this should be loaded from json");
            }
            case USE -> {
                if (this.belongsToPlayer()) {
                    this.getParent().removeChild(this);
                    this.react(PlayerAction.EQUIP);
                }
                else { //note: no feature should ever have null as a parent, except for Rooms
                    this.react(PlayerAction.PICKUP);
                }
            }
            case EQUIP -> {
                this.setParent(playerAction.getPlayer().getPlayerAsFeature());
            }
            default -> {
                this.onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The armor has no keyhole of any kind! What are you playing at?");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("For some odd reason, you think that attacking a piece of armor, something designed specifically to withstand attacks, isn't a very good idea...");
            }
        }
    }
}
