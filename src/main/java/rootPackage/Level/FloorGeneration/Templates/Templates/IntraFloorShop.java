package rootPackage.Level.FloorGeneration.Templates.Templates;

import rootPackage.Level.Features.Enemy.Bosses.Minotaur;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.Features.*;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

public class IntraFloorShop extends RoomTemplate {

    public IntraFloorShop() {
        this.featuresNeeded = new Feature[4];
        featuresNeeded[0] = new HealthMerchant();
        featuresNeeded[1] = new DamageMerchant();
        featuresNeeded[2] = new DefenseMerchant();
        featuresNeeded[3] = new Entry();
        this.roomDescription = "The room is nearly devoid of all matter, save for some merchants and a path leading downwards...";
    }

}
