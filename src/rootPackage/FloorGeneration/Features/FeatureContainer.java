package rootPackage.FloorGeneration.Features;

import java.util.List;

/**
 * An interface used by features that have the ability to hold more features within them
 */
public interface FeatureContainer {

    List<Feature> containedFeatures = null;

    public List<Feature> getContainedFeatures();

}
