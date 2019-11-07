package unsw.dungeon.gameplay;

import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import unsw.dungeon.MapObjectGroup;

public abstract class MapObject {
  private HashMap<String, MapObjectState> states;
  private ObjectProperty<Cell> cell;
  private StringProperty image;
  private MapObjectGroup group;

  public MapObject() {
    this.cell = new SimpleObjectProperty<Cell>(null);
    this.states = new HashMap<>();
    this.image = new SimpleStringProperty(this.initialImage());
  }

  protected MapObjectGroup getMapObjectGroup() {
    return group;
  }

  public void addToMapObjectGroup(MapObjectGroup group) {
    this.group = group;
  }

  public void initProperties(HashMap<String, Object> properties) {}

  public ObjectProperty<Cell> cell() {
    return cell;
  }

  public StringProperty image() {
    return image;
  }

  public Cell getCell() {
    return cell.get();
  }

  public String getImage() {
    return image.get();
  }

  public abstract String initialImage();

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
    states.remove(state.getName());
  }

  protected abstract boolean canWalkInto(MapObject object);

  public void moveTo(Cell next) {
    if (!next.canWalkInto(this)) {
      return;
    }
    if (getCell() != null) {
      this.getCell().removeMapObject(this);
    }
    this.cell.set(next);
    next.addMapObject(this);
    Player player = (Player) next.getMapObjectOfType(Player.class);
    if (player != null) {
      this.playerInteraction(null, player);
    }
  }

  public void moveTo(int direction) {
    if (getCell() != null && getCell().getAdjacentCell(direction) != null)
      this.moveTo(getCell().getAdjacentCell(direction));
  }

  protected abstract void playerInteraction(Cell start, Player player);

  protected void removeFromCell(Boolean decrementCounter) {
    if (this.getCell() != null) {
      this.getCell().removeMapObject(this);
      this.cell.set(null);
    }
    if (decrementCounter) this.group.decrementCounter();
  }

  protected void removeFromCell() {
    this.removeFromCell(true);
  }

  protected abstract StringBuilder printCLI();
}
