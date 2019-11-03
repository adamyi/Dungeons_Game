package unsw.dungeon.objectives;

import java.util.ArrayList;
import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.MapObject;

public class FOLOrObjectiveNode implements ObjectiveNode {
  private ArrayList<ObjectiveNode> children;

  public FOLOrObjectiveNode() {
    this.children = new ArrayList<>();
  }

  public void addChild(ObjectiveNode child) {
    if (child != null) this.children.add(child);
  }

  public Boolean hasWon(HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups) {
    if (children.isEmpty()) return true;
    for (ObjectiveNode child : children) {
      if (child.hasWon(mapObjectGroups)) return true;
    }
    return false;
  }
}
