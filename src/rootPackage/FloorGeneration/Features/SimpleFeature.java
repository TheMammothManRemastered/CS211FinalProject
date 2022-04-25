package rootPackage.FloorGeneration.Features;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import rootPackage.FloorGeneration.FloorGenerationRNG;
import rootPackage.FloorGeneration.Room;
import rootPackage.Player;

import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class SimpleFeature extends Feature {

    private String interactMessage;

    public SimpleFeature(String[] names, String locationInRoom, String description, String interactMessage) {
        super(names, locationInRoom);
        this.setDescription(description);
        this.interactMessage = interactMessage;
    }

    public SimpleFeature(String[] names, String locationInRoom, Room associatedRoom, String description, String interactMessage) {
        super(names, locationInRoom, associatedRoom);
        this.setDescription(description);
        this.interactMessage = interactMessage;
    }

    public String getInteractMessage() {
        return interactMessage;
    }

    public static void main(String[] args) {
        SimpleFeature simpleFeature = randomSimpleFeature("ceiling");
        simpleFeature.onExamine(null);
        simpleFeature.onInteract(null);
    }

    public static SimpleFeature randomSimpleFeature(String locationInRoom) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("json/simpleFeatures.json");

            // parse the wider json file
            JSONObject json = (JSONObject) jsonParser.parse(reader);
            JSONArray simpleFeaturesJson = (JSONArray) json.get("simpleFeatures");

            // get one of the entries at random
            int numSimpleFeatures = simpleFeaturesJson.size();
            JSONObject selectedFeature = (JSONObject) simpleFeaturesJson.get(FloorGenerationRNG.rng.nextInt(numSimpleFeatures));

            JSONArray namesjson = (JSONArray) selectedFeature.get("names");
            String[] names = new String[namesjson.size()];
            for (int i = 0; i < namesjson.size(); i++) {
                Object obj = namesjson.get(i);
                String val = (String) obj;
                names[i] = val;
            }
            String description = (String) selectedFeature.get("description");
            String onInteract = (String) selectedFeature.get("onInteract");

            return new SimpleFeature(names, locationInRoom, description, onInteract);

        } catch (FileNotFoundException exception) {
            System.out.println("fatal - simpleFeatures.json could not be found!");
            System.exit(-1);
        } catch (ParseException e) {
            System.out.println("fatal - simpleFeatures.json could not be parsed!");
            System.exit(-1);
        } catch (IOException exception) {
            System.out.println("fatal - simpleFeatures.json could not be accessed!");
            System.exit(-1);
        }
        return null;
    }

    public static SimpleFeature randomSimpleFeature(String locationInRoom, Room associatedRoom) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("simpleFeatures.json");

            // parse the wider json file
            JSONObject json = (JSONObject) jsonParser.parse(reader);
            JSONArray simpleFeaturesJson = (JSONArray) json.get("simpleFeatures");

            // get one of the entries at random
            int numSimpleFeatures = simpleFeaturesJson.size();
            JSONObject selectedFeature = (JSONObject) simpleFeaturesJson.get(FloorGenerationRNG.rng.nextInt(numSimpleFeatures));

            String[] names = (String[]) selectedFeature.get("names");
            String description = (String) selectedFeature.get("description");
            String onInteract = (String) selectedFeature.get("onInteract");

            SimpleFeature sf = new SimpleFeature(names, locationInRoom, associatedRoom, description, onInteract);

            return sf;

        } catch (FileNotFoundException exception) {
            System.out.println("fatal - simpleFeatures.json could not be found!");
            System.exit(-1);
        } catch (ParseException e) {
            System.out.println("fatal - simpleFeatures.json could not be parsed!");
            System.exit(-1);
        } catch (IOException exception) {
            System.out.println("fatal - simpleFeatures.json could not be accessed!");
            System.exit(-1);
        }
        return null;
    }

    @Override
    public void onExamine(Player player) {
        System.out.println(getDescription());
    }

    public void onInteract(Player player) {
        System.out.println(interactMessage);
    }
}