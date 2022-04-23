package rootPackage.FloorGeneration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.FloorGeneration.Features.Feature;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class responsible for generating a floor theme from a given difficulty.
 *
 * @author William Owens
 */
public class FloorThemeGenerator {

    private static String FLOOR_THEMES_PATH = "json" + System.getProperty("file.separator") + "floorGeneration" + System.getProperty("file.separator") + "floorThemes" + System.getProperty("file.separator");

    public static void main(String[] args) {
        generateFloorTheme(1);
    }

    public static FloorTheme generateFloorTheme(int difficulty) {
        String[] floorThemeNames = getFloorNamesForGivenDifficulty(difficulty);

        try {
            // pick a theme at random
            String selectedThemeName = floorThemeNames[FloorGenerationRNG.rng.nextInt(floorThemeNames.length)];

            // grab the theme's JSON file
            JSONObject selectedThemeJSON = null;
            try {
                JSONParser parser = new JSONParser();
                FileReader reader = new FileReader(FLOOR_THEMES_PATH + selectedThemeName);
                selectedThemeJSON = (JSONObject) parser.parse(reader);
            } catch (IOException exception) {
                System.out.printf("fatal - json/floorGeneration/floorThemes/%s could not be accessed!%n", selectedThemeName);
                System.exit(-1);
            } catch (ParseException exception) {
                System.out.printf("fatal - json/floorGeneration/floorThemes/%s could not be parsed!%n", selectedThemeName);
                System.exit(-1);
            }

            // parse the selected theme into a FloorTheme object
            int minimumSize = (int) selectedThemeJSON.get("minimumSize");
            int maximumSize = minimumSize + FloorGenerationRNG.rng.nextInt(difficulty * 3);
            int deadEndsNeeded = (int) selectedThemeJSON.get("deadEndsNeeded");
            int numEnemies = (int) selectedThemeJSON.get("minimumEnemies");
            numEnemies += FloorGenerationRNG.rng.nextInt(difficulty * 3);
            int healthPoints = (int) selectedThemeJSON.get("healthPoints");
            String commonEnemyType = (String) selectedThemeJSON.get("commonEnemyType");
            Feature[] enemies = new Feature[numEnemies];
            for (int i = 0; i < numEnemies; i++) {
                boolean isCommonEnemy = 3 < FloorGenerationRNG.rng.nextInt(11);
                if (isCommonEnemy)
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Generates the names of all floors matching the given difficulty value.
     */
    private static String[] getFloorNamesForGivenDifficulty(int difficulty) {
        // declare a parser
        JSONParser parser = new JSONParser();
        // declare this here as well, since it gets used outside of the try catch
        String[] floorThemeNames = new String[0];
        try {
            // get possible floor themes for the given difficulty
            floorThemeNames = generateFloorThemeNames(difficulty, parser);
        } catch (FileNotFoundException exception) {
            System.out.println("fatal - json/floorGeneration/floorThemes/themesIndex.json could not be found!");
            System.exit(-1);
        } catch (ParseException exception) {
            System.out.println("fatal - json/floorGeneration/floorThemes/themesIndex.json could not be parsed!");
            System.exit(-1);
        } catch (IOException exception) {
            System.out.println("fatal - json/floorGeneration/floorThemes/themesIndex.json could not be accessed!");
            System.exit(-1);
        }
        return floorThemeNames;
    }

    // helper for getFloorNamesForGivenDifficulty, makes the code a bit more readable
    private static String[] generateFloorThemeNames(int difficulty, JSONParser parser) throws IOException, ParseException {
        // read the themes index file and parse it into a JSONArray
        FileReader reader = new FileReader(FLOOR_THEMES_PATH + "themesIndex.json");
        JSONObject indexFileJson = (JSONObject) parser.parse(reader);
        JSONArray floorThemesArray = (JSONArray) indexFileJson.get(Integer.toString(difficulty));

        // setup the output array, convert items from JSON array into the names array
        String[] floorThemeNames = new String[floorThemesArray.size()];
        for (int i = 0; i < floorThemeNames.length; i++) {
            String jsonValue = (String) floorThemesArray.get(i);
            floorThemeNames[i] = jsonValue;
        }
        return floorThemeNames;
    }

}
