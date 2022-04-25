package rootPackage.FloorGeneration.Features.Concrete;

import rootPackage.FloorGeneration.Features.Feature;
import rootPackage.FloorGeneration.Features.FeatureContainer;

import java.io.IOException;

public class Pillar extends Feature implements Drawable {

    public Pillar(FeatureContainer container) {
        super(new String[]{"pillar", "pillars", "column", "columns"}, "Pillars", "", container);
    }

}
