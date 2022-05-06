package rootPackage.Level.Features.Features;

import rootPackage.Graphics.GUI.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Equipment.AccessoryFeature;
import rootPackage.Level.Features.Equipment.EquipmentAlias;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;
import rootPackage.Player;

import java.util.ArrayList;

public class HealthMerchant extends Feature {

    public HealthMerchant() {
        primaryName = "Health Merchant";
        allNames = new String[]{};
        sprite = new Sprite(RenderLayer.WALL_DECO, "healthMerchant.png");
    }

    private int calcGold(Player player) {
        ArrayList<Feature> accessories = player.getPlayerAsFeature().getChildren(FeatureFlag.ACCESSORY);
        int mult = 0;
        for (Feature feature : accessories) {
            if (feature.getPrimaryName().equals("Health Talisman")) {
                mult++;
            }
        }
        return (120 + (120 * mult));
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The health merchant will sell you health increases, for a modest sum. Currently, he charges %d gold for one increase.".formatted(calcGold(Main.player)));
            }
            case USE -> {
                if (Main.player.getGold() >= calcGold(Main.player)) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You hand over %d gold. A health talisman appears in your hands!".formatted(calcGold(Main.player)));
                    Main.player.addGold(-calcGold(Main.player));
                    Main.player.getPlayerAsFeature().addChild(EquipmentAlias.getEquipment("healthTalisman"));
                } else {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You don't have enough gold! In other words, you don't have the right!");
                }
            }
            default -> {
                onActionNotApplicable(playerAction);
            }
        }
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        switch (playerAction) {
            case PICKUP, EQUIP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The health merchant would rather not be moved.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You aren't holding the health merchant!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The health merchant isn't locked.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking a defenceless merchant? How low can you go?");
            }
        }
    }
}
