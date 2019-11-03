package unsw.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import unsw.dungeon.gameplay.*;

public class MapObjectHelper {

  private ArrayList<MapObjectWrapper> enabledMapObjectTypes;

  private class MapObjectWrapper<T extends MapObject> {
    private Class type;
    private String name;
    private String objectiveName;
    private Supplier<T> supplier;

    MapObjectWrapper(Class type, Supplier<T> supplier, String name, String objectiveName) {
      this.type = type;
      this.supplier = supplier;
      this.name = name;
      this.objectiveName = objectiveName;
    }

    protected MapObjectGroup<T> createMapObjectGroup() {
      return new MapObjectGroup<T>(supplier);
    }

    protected String getName() {
      return name;
    }

    protected String getObjectiveName() {
      return name;
    }

    protected Class getType() {
      return type;
    }
  }

  // add mapobject types here as configuration
  public MapObjectHelper() {
    this.enabledMapObjectTypes = new ArrayList<>();
    enabledMapObjectTypes.add(
        new MapObjectWrapper<Player>(Player.class, Player::new, "player", null));
    enabledMapObjectTypes.add(
        new MapObjectWrapper<Enemy>(Enemy.class, Enemy::new, "enemy", "enemies"));
  }

  public HashMap<String, Class<? extends MapObject>> getMapObjectStringToClass() {
    HashMap<String, Class<? extends MapObject>> ret = new HashMap<>();
    for (MapObjectWrapper mow : enabledMapObjectTypes) {
      ret.put(mow.getName(), mow.getType());
    }
    return ret;
  }

  public HashMap<String, Class<? extends MapObject>> getObjectiveStringToClass() {
    HashMap<String, Class<? extends MapObject>> ret = new HashMap<>();
    for (MapObjectWrapper mow : enabledMapObjectTypes) {
      String objName = mow.getObjectiveName();
      if (objName != null) {
        ret.put(objName, mow.getType());
      }
    }
    return ret;
  }

  public HashMap<Class<? extends MapObject>, MapObjectGroup> newMapObjectGroups() {
    HashMap<Class<? extends MapObject>, MapObjectGroup> ret = new HashMap<>();
    for (MapObjectWrapper mow : enabledMapObjectTypes) {
      ret.put(mow.getType(), mow.createMapObjectGroup());
    }
    return ret;
  }
}
