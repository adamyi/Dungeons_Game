package unsw.dungeon.gameengine.objectives;

import java.util.HashMap;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.gameplay.MapObject;

public class LeafObjectiveNode implements ObjectiveNode {
  private Class<? extends MapObject> type;

  public LeafObjectiveNode(Class<? extends MapObject> type) {
    this.type = type;
  }

  public Boolean hasWon(HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups) {
    MapObjectGroup group = mapObjectGroups.get(type);
    if (group != null && group.getCounter() == 0) return true;
    return false;
  }
}
