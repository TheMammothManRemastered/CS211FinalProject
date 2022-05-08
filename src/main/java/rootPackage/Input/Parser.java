package rootPackage.Input;

import org.json.simple.*;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class responsible for parsing player input into a game command.
 * Uses purely static methods.
 *
 * @version 1.22
 * @author William Owens
 */
public class Parser {

    private static final ArrayList<String> FILLER_WORDS = new ArrayList<>(Arrays.asList(
            "a",
            "an",
            "the",
            "is",
            "and",
            "of",
            "then",
            "all",
            "but",
            "one",
            "but",
            "except",
            ".",
            ",",
            "\"",
            "with",
            "up",
            "down",
            "to"
    ));

    //TODO: this data is *basically* implemented twice, here and in playerActionAlias, try and remove that redundancy
    private static final ArrayList<String> VERBS = new ArrayList<>(Arrays.asList(
            "take",
            "grab",
            "steal",
            "pick",
            "obtain",
            "leave",
            "drop",
            "discard",
            "remove",
            "put",
            "look",
            "examine",
            "view",
            "check",
            "unlock",
            "unlatch",
            "interact",
            "use",
            "activate",
            "press",
            "open",
            "attack",
            "engage",
            "kill",
            "charge",
            "don",
            "doff",
            "move",
            "travel",
            "equip",
            "wear",
            "destroy",
            "decimate",
            "terminate",
            "eliminate",
            "stab",
            "shoot",
            "inspect",
            "fight",
            "battle"
    ));

    /**
     * Parses a player's input to get the user's selected action.
     */
    public static PlayerAction getActionFromInput(String input) {
        input = input.toLowerCase();
        String[] words = input.split(" ");
        String verbAlias = words[0];
        return PlayerActionAlias.aliasToAction(verbAlias);
    }

    /**
     * Parses a player's input to get the object the user has indicated. Checks just the room the player is in.
     */
    public static Feature getObjectFromInput(String input) {
        input = input.toLowerCase();
        String[] words = input.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (isFiller(words[i])) {
                words[i] = "";
            }
            if (isVerb(words[i])) {
                words[i] = "";
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : words) {
            if (s.isEmpty())
                continue;
            sb.append(s);
            sb.append(' ');
        }

        Feature output = Main.player.getCurrentRoom().getRoomAsFeature().getChildWithName(sb.toString());
        sb = null;
        return output;
    }

    private static boolean isFiller(String input) {
        return FILLER_WORDS.contains(input);
    }

    private static boolean isVerb(String input) {
        return VERBS.contains(input);
    }

}
