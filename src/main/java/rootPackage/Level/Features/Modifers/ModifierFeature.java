package rootPackage.Level.Features.Modifers;

import rootPackage.Battle.StatusEffects.StatusEffects;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

/**
 * An invisible {@link rootPackage.Level.Features.Feature Feature} containing a status effect to apply to the player during a battle.
 *
 * @version 1.0
 * @author William Owens
 */
public class ModifierFeature extends Feature {

    private final StatusEffects effect;
    private final boolean defaultApplicability;

    public ModifierFeature(StatusEffects effect, boolean defaultApplicability) {
        this.effect = effect;
        this.defaultApplicability = defaultApplicability;
    }

    public ModifierFeature(String primaryName, String[] allNames, StatusEffects effect, boolean defaultApplicability) {
        super(primaryName, allNames);
        this.effect = effect;
        this.defaultApplicability = defaultApplicability;
    }

    public StatusEffects getEffect() {
        return effect;
    }

    //TODO: check if the effect is applicable given the conditions of a battle
    public boolean isApplicable() {
        return defaultApplicability;
    }

    // must be implemented bc it's a Feature, but these will never ever ever be called
    @Override
    public void react(PlayerAction playerAction) {
        this.onActionNotApplicable(playerAction);
    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {
        Main.mainWindow.getConsoleWindow().addEntryToHistory("This message should never appear!");
    }
}
