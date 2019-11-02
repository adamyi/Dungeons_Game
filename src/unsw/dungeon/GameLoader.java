package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import unsw.dungeon.gameplay.*;

/** Loads a game from a .json file. */
public class GameLoader {

  private JSONObject json;

  private HashMap<String, Class<? extends MapObject>> typeToMapObjectClass;

  public GameLoader(String filename) throws FileNotFoundException {
    json = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/dungeons/" + filename)));
    MapObjectHelper moh = new MapObjectHelper();
    this.typeToMapObjectClass = moh.getMapObjectStringToClass();
  }

  /**
   * Parses the JSON to create a dungeon.
   *
   * @return
   */
  public Game load() {
    int width = json.getInt("width");
    int height = json.getInt("height");

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
        game.addMapObject(typeclass, y, x, obj);
      }
    }

    // TODO: parse objective
    return game;
  }
}
