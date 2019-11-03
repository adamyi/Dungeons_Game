package unsw.dungeon.objectives;

import java.util.ArrayList;
import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.MapObject;

public class FOLAndObjectiveNode implements ObjectiveNode {
  private ArrayList<ObjectiveNode> children;

  public FOLAndObjectiveNode() {
    this.children = new ArrayList<>();
  }

  public void addChild(ObjectiveNode child) {
    if (child != null) this.children.add(child);
  }

  public Boolean hasWon(HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups) {
    for (ObjectiveNode child : children) {
      if (!child.hasWon(mapObjectGroups)) return false;
    }
    return true;
  }
}
