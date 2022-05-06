package rootPackage.Level.FloorGeneration.Templates.Templates;

import rootPackage.Level.Features.Enemy.Bosses.Minotaur;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.Features.Bloodstain;
import rootPackage.Level.Features.Features.Exit;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

public class MinotaurBossRoom extends RoomTemplate {

    public MinotaurBossRoom() {
        this.featuresNeeded = new Feature[4];
        featuresNeeded[2] = new Bloodstain();
        featuresNeeded[0] = new Minotaur();
        Bloodstain bs = new Bloodstain(2);
        bs.setExamineText("");
        featuresNeeded[1] = bs;
        featuresNeeded[3] = new Exit();
        this.roomDescription = "The floor is littered with corpses and blood. Surely, this is the den of a dangerous foe...";
    }

}
