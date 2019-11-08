package unsw.dungeon.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MapUtils {
  public static HashMap<String, JSONObject> getMaps(Class c)
      throws URISyntaxException, UnsupportedEncodingException, IOException {
    HashMap<String, JSONObject> maps = new HashMap<>();

    URL url = c.getResource("/dungeons");
    String path = url.getPath();
    String jarPath = path.substring(5, path.indexOf("!"));
    System.out.println(jarPath);
    try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        String name = entry.getName();
        if (name.startsWith("dungeons") && name.endsWith(".json")) {
          JSONObject map = new JSONObject(new JSONTokener(c.getResourceAsStream("/" + name)));
          String mapName = name.substring(9, name.length() - 5);
          if (map.has("name")) mapName = map.getString("name");
          maps.put(mapName, map);
        }
      }
    }
    return maps;
  }

  public static String getWinningConditionsTextFromMap(JSONObject goal) {
    String ret = getWinningConditionsText(goal.getJSONObject("goal-condition"));
    if (ret.startsWith("(")) ret = ret.substring(1, ret.length() - 1);
    return ret;
  }

  public static String getWinningConditionsText(JSONObject goal) {
    String type = goal.getString("goal");
    if ("AND".equals(type) || "OR".equals(type)) {
      StringBuilder sb = new StringBuilder("(");
      JSONArray subgoals = goal.getJSONArray("subgoals");
      for (int i = 0; i < subgoals.length(); i++) {
        if (i > 0) sb.append(" ").append(type).append(" ");
        JSONObject subgoal = subgoals.getJSONObject(i);
        sb.append(getWinningConditionsText(subgoal));
      }
      sb.append(")");
      return sb.toString();
    }
    switch (type) {
      case "exit":
        return "Getting to an exit";
      case "enemies":
        return "Destroying all enemies";
      case "boulders":
        return "Having a boulder on all floor switches";
      case "treasure":
        return "Collecting all treasure";
    }
    return "";
  }
}
