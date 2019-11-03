package unsw.dungeon.gameplay;

import java.util.HashMap;
import unsw.dungeon.MapObjectGroup;

public abstract class MapObject {
  private HashMap<String, MapObjectState> states;
  private Cell cell;
  private MapObjectGroup group;

  public MapObject() {
    this.cell = null;
    this.states = new HashMap<>();
  }

  protected MapObjectGroup getMapObjectGroup() {
    return group;
  }

  public void addToMapObjectGroup(MapObjectGroup group) {
    this.group = group;
  }

  public void initProperties(HashMap<String, Object> properties) {}

  public Cell getCell() {
    return cell;
  }

  protected void setState(MapObjectState state) {
    String name = state.getName();
    if (!state.isActive()) {
      System.out.println("trying to add expired state " + name + ". Ignoring");
    } else {
      if (states.get(name) != null) System.out.println("Warning: overriding state " + name);
      states.put(name, state);
    }
  }

  // NOTES(adamyi@): there's a race condition vuln here. But #WONTFIX for now since we are currently
  // using single thread.
  protected void setState(String name, int seconds) {
    MapObjectState state = this.getState(name);
    if (state == null) {
      state = new MapObjectState(name, seconds);
      states.put(name, state);
    } else {
      state.extendDeadline(seconds);
    }
  }

  protected void setState(String name) {
    this.setState(name, Integer.MAX_VALUE);
  }

  // implements lazy evaluation for state invalidation
  protected MapObjectState getState(String name) {
    MapObjectState state = states.get(name);
    if (state == null) return null;
    if (!state.isActive()) {
      states.remove(state);
      return null;
    }
    return state;
  }

  protected void removeState(String name) {
    states.remove(name);
  }

  protected void removeState(MapObjectState state) {
    states.remove(state);
  }

  protected abstract boolean canWalkInto(MapObject object);

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
    this.moveTo(cell.getAdjacentCell(direction));
  }

  protected abstract void playerInteraction(Cell start, Player player);

  protected void removeFromCell() {
    if (this.cell != null) {
      this.cell.removeMapObject(this);
      this.cell = null;
    }
  }
}
