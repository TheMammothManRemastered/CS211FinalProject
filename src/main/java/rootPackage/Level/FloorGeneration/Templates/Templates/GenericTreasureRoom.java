package rootPackage.Level.FloorGeneration.Templates.Templates;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rootPackage.Level.Features.Equipment.EquipmentAlias;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.Features.Pedestal;
import rootPackage.Level.FloorGeneration.FloorGenerationRNG;
import rootPackage.Level.FloorGeneration.Templates.RoomTemplate;

import java.io.FileReader;
import java.io.IOException;

public class GenericTreasureRoom extends RoomTemplate {

    public GenericTreasureRoom() {
        this.featuresNeeded = new Feature[1];
        Pedestal pedestal = new Pedestal();
        JSONParser parser = new JSONParser();
        JSONObject genericJsonObject = null;
        try {
            genericJsonObject = (JSONObject) parser.parse(new FileReader("json"+System.getProperty("file.separator")+"equipment"+System.getProperty("file.separator")+"genericTreasure.json"));
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
        JSONArray treasures = (JSONArray) genericJsonObject.get("treasures");
        int selectedTreasure = FloorGenerationRNG.rng.nextInt(treasures.size());
        pedestal.addChild(EquipmentAlias.getEquipment((String) treasures.get(selectedTreasure)));
        featuresNeeded[0] = pedestal;
        this.roomDescription = "This room is bathed in light emanating from the pedestal at its center. What is that you see on top of it?";
    }

}
