package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import unsw.dungeon.gameplay.*;
import unsw.dungeon.objectives.*;
import unsw.dungeon.utils.JSONUtils;

/** Loads a game from a .json file. */
public class GameLoader {
  public static final int MAX_MAP_EDGE_SIZE = 64;

  private JSONObject json;

  private HashMap<String, Class<? extends MapObject>> typeToMapObjectClass;
  private HashMap<String, Class<? extends MapObject>> objectiveStringToMapObjectClass;

  public GameLoader(String filename) throws FileNotFoundException {
    json = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/dungeons/" + filename)));
    MapObjectHelper moh = new MapObjectHelper();
    this.typeToMapObjectClass = moh.getMapObjectStringToClass();
    this.objectiveStringToMapObjectClass = moh.getObjectiveStringToClass();
  }

  /**
   * Parses the JSON to create a dungeon.
   *
   * @return
   */
  public Game load() {
    int width = json.getInt("width");
    int height = json.getInt("height");

    if (width > MAX_MAP_EDGE_SIZE) {
      throw new RuntimeException("map too wide");
    }
    if (height > MAX_MAP_EDGE_SIZE) {
      throw new RuntimeException("map too tall");
    }

    Game game = new Game(height, width);

    JSONArray jsonObjects = json.getJSONArray("entities");

    for (int i = 0; i < jsonObjects.length(); i++) {
      JSONObject obj = jsonObjects.getJSONObject(i);
      String type = obj.getString("type");
      int x = obj.getInt("x");
      int y = obj.getInt("y");
      Class typeclass = typeToMapObjectClass.get(type);
      if (typeclass == null) {
        System.out.printf("[DEBUG] Unknown map object type: %s at (%d, %d)\n", type, x, y);
      } else {
        System.out.printf("[DEBUG] adding map object type: %s at (%d, %d)\n", type, x, y);
        try {
          HashMap<String, Object> properties = JSONUtils.jsonToMap(obj);
          game.addMapObject(typeclass, y, x, properties);
        } catch (JSONException e) {
          System.out.printf(
              "[DEBUG] JSONException: error adding map object type: %s at (%d, %d)\n", type, x, y);
        }
      }
    }

    game.setObjective(parseObjective(json.getJSONObject("goal-condition")));

    return game;
  }

  private ObjectiveNode parseObjective(JSONObject objective) {
    String type = objective.getString("goal");
    if ("AND".equals(type)) {
      FOLAndObjectiveNode objNode = new FOLAndObjectiveNode();
      JSONArray subgoals = objective.getJSONArray("subgoals");
      for (int i = 0; i < subgoals.length(); i++) {
        JSONObject subgoal = subgoals.getJSONObject(i);
        objNode.addChild(parseObjective(subgoal));
      }
      return objNode;
    }
    if ("OR".equals(type)) {
      FOLOrObjectiveNode objNode = new FOLOrObjectiveNode();
      JSONArray subgoals = objective.getJSONArray("subgoals");
      for (int i = 0; i < subgoals.length(); i++) {
        JSONObject subgoal = subgoals.getJSONObject(i);
        objNode.addChild(parseObjective(subgoal));
      }
      return objNode;
    }
    if (this.objectiveStringToMapObjectClass.get(type) != null) {
      LeafObjectiveNode objNode =
          new LeafObjectiveNode(this.objectiveStringToMapObjectClass.get(type));
      return objNode;
    }
    System.out.printf("Unknown objective type: %s. Ignored.\n", type);
    return null;
  }
}
