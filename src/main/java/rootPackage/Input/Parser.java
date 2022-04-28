package rootPackage.Input;

import rootPackage.Level.Features.Feature;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class responsible for parsing player input into a game command.
 * Uses purely static methods.
 *
 * @version 1.0
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
            "down"
    ));

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
            "charge"
    ));

    public static PlayerAction getActionFromInput(String input) {
        input = input.toLowerCase();
        String[] words = input.split(" ");
        String verbAlias = words[0];
        return PlayerActionAlias.aliasToAction(verbAlias);
    }

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
            sb.append(s);
        }

        //TODO: this

        return null;

    }

    private static boolean isFiller(String input) {
        return FILLER_WORDS.contains(input);
    }

    private static boolean isVerb(String input) {
        return VERBS.contains(input);
    }

}
