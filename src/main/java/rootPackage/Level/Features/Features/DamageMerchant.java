package rootPackage.Level.Features.Features;

import org.json.simple.*;
import rootPackage.Graphics.Viewport.RenderLayer;
import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Equipment.EquipmentAlias;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;
import rootPackage.Player;

import java.util.ArrayList;
import java.util.Objects;

public class DamageMerchant extends Feature {

    public DamageMerchant() {
        primaryName = "Attack Merchant";
        allNames = new String[]{};
        sprite = new Sprite(RenderLayer.WALL_DECO, "attackMerchant.png");
    }

    private int calcGold(Player player) {
        ArrayList<Feature> accessories = player.getPlayerAsFeature().getChildren(FeatureFlag.ACCESSORY);
        int mult = 0;
        for (Feature feature : accessories) {
            if (feature.getPrimaryName().equals("Attack Talisman")) {
                mult++;
            }
        }
        return (70 + ((70 * mult) / 3));
    }

    @Override
    public void react(PlayerAction playerAction) {
        switch (playerAction) {
            case EXAMINE -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The attack merchant will sell you damage increases, for a modest sum. Currently, he charges %d gold for one increase.".formatted(calcGold(Main.player)));
            }
            case USE -> {
                if (Main.player.getGold() >= calcGold(Main.player)) {
                    Main.mainWindow.getConsoleWindow().addEntryToHistory("You hand over %d gold. An attack talisman appears in your hands!".formatted(calcGold(Main.player)));
                    Main.player.addGold(-calcGold(Main.player));
                    Main.player.getPlayerAsFeature().addChild(Objects.requireNonNull(EquipmentAlias.getEquipment("attackTalisman")));
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
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The attack merchant would rather not be moved.");
            }
            case DROP -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("You aren't holding the attack merchant!");
            }
            case UNLOCK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("The attack merchant isn't locked.");
            }
            case ATTACK -> {
                Main.mainWindow.getConsoleWindow().addEntryToHistory("Attacking a defenceless merchant? How low can you go?");
            }
        }
    }

}
