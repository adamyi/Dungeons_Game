package unsw.dungeon.gameengine.objectives;

import java.util.HashMap;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.gameplay.MapObject;

public interface ObjectiveNode {
  public abstract Boolean hasWon(
      HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups);
}
