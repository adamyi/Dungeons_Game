package unsw.dungeon.objectives;

import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.MapObject;

public interface ObjectiveNode {
  public abstract Boolean hasWon(
      HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups);
}
