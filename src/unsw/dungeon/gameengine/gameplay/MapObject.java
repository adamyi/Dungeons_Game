package unsw.dungeon.gameengine.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.Observer;
import unsw.dungeon.gameengine.Subject;

public abstract class MapObject implements Subject {
  private HashMap<String, MapObjectState> states;
  private ObjectProperty<Cell> cell;
  private StringProperty image;
  private DoubleProperty hue;
  private List<Observer> observers;
  private MapObjectGroup group;
  private Integer id;
  private String typeString;

  public MapObject() {
    this.cell = new SimpleObjectProperty<Cell>(null);
    this.states = new HashMap<>();
    this.image = new SimpleStringProperty(this.initialImage());
    this.hue = new SimpleDoubleProperty(0);
    this.observers = new ArrayList<>();
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

  public DoubleProperty hue() {
    return hue;
  }

  public Cell getCell() {
    return cell.get();
  }

  public void setCell(Cell cell) {
    this.cell.set(cell);
    this.notifyObservers();
  }

  public String getImage() {
    return this.image.get();
  }

  public void setImage(String image) {
    this.image.set(image);
    this.notifyObservers();
  }

  public Double getHue() {
    return hue.get();
  }

  public void setHue(Double hue) {
    this.hue.set(hue);
    this.notifyObservers();
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
    this.setCell(next);
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

  public void removeFromCell(Boolean decrementCounter) {
    if (this.getCell() != null) {
      this.getCell().removeMapObject(this);
      this.setCell(null);
    }
    if (decrementCounter) this.group.decrementCounter();
  }

  public void removeFromCell() {
    this.removeFromCell(true);
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setTypeString(String typeString) {
    this.typeString = typeString;
  }

  public String getTypeString() {
    return typeString;
  }

  protected abstract StringBuilder printCLI();

  public abstract double viewOrder();

  @Override
  public void attach(Observer observer) {
    this.observers.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    this.observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : this.observers) observer.update(this);
  }
}
