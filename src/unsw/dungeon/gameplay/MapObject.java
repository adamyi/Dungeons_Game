package unsw.dungeon.gameplay;

import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;

public abstract class MapObject {
  private HashMap<String, MapObjectState> states;
  private Cell cell;
  private MapObjectGroup group;

  public MapObject(MapObjectGroup group) {
    this.cell = null;
    this.group = group;
    this.states = new HashMap<>();
  }

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
    throw new UnsupportedOperationException();
  }

  public void moveTo(int direction) {
    this.moveTo(cell.getAdjacentCell(direction));
  }

  protected abstract void playerInteraction(Cell next, Player player);

  private void removeFromCell() {
    this.cell.removeMapObject(this);
    this.cell = null;
  }
}