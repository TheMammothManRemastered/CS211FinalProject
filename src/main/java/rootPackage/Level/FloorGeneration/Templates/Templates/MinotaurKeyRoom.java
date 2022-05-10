package rootPackage.Level.FloorGeneration.Templates.Templates;

import rootPackage.Level.Features.Enemy.Enemies.WolfPack;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.Features.Meat;
import rootPackage.Level.Features.Features.Pedestal;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

public class MinotaurKeyRoom extends RoomTemplate {

    public MinotaurKeyRoom() {
        // pedestal, wolfpack, meat on the pedestal, skeleton on the floor?
        this.featuresNeeded = new Feature[2];
        featuresNeeded[0] = new WolfPack();
        Pedestal pedestal = new Pedestal();
        pedestal.addChild(new Meat());
        featuresNeeded[1] = pedestal;
        this.roomDescription = "The floor is littered with corpses and blood. Surely, this is the den of a dangerous foe...";
    }

}
