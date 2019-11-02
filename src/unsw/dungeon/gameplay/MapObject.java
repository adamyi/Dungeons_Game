package unsw.dungeon.gameplay;

import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;

public abstract class MapObject {
  protected HashMap<String, MapObjectState> states;
  protected Cell cell;
  protected MapObjectGroup group;

  public MapObject() {
    this.cell = null;
    this.states = new HashMap<>();
  }

  public void addToMapObjectGroup(MapObjectGroup group) {
    this.group = group;
  }

  public void initProperties(HashMap<String, Object> properties) {}

  public Cell getCell() {
    return cell;
  }

  protected void setState(MapObjectState state) {
    states.put(state.getName(), state);
  }

  protected void setState(String name, int seconds) {
    MapObjectState state = states.get(name);
    if (state == null) {
      state = new MapObjectState(name);
      states.put(name, state);
    }
    state.extendDeadline(seconds);
  }

  protected MapObjectState getState(String name) {
    return states.get(name);
  }

  protected void removeState(String name) {
    states.remove(name);
  }

  protected abstract boolean canWalkInto(Entity entity, Cell next);

  protected void moveTo(Cell next) {
    this.moveTo(Direction.UNKNOWN, next);
  }

  protected void moveTo(int direction, Cell next) {
    this.cell.removeMapObject(this);
    this.cell = next;
    next.addMapObject(this);
    // if next contains player
    // next.playerInteraction(player)
  }

  public void moveTo(int direction) {
    this.moveTo(direction, cell.getAdjacentCell(direction));
  }

  protected abstract void playerInteraction(Cell next, Player player);

  protected void removeFromCell() {
    if (this.cell != null) {
      this.cell.removeMapObject(this);
      this.cell = null;
    }
  }
}
