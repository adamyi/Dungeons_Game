package unsw.dungeon;

import java.util.ArrayList;
import java.util.function.Supplier;
import unsw.dungeon.gameplay.AI;
import unsw.dungeon.gameplay.MapObject;

public class MapObjectGroup<T extends MapObject> {
  private Supplier<T> supplier;
  private int counter;
  private ArrayList<T> mapObjects;

  public MapObjectGroup(Supplier<T> supplier) {
    this.supplier = supplier;
    this.mapObjects = new ArrayList<T>();
    this.counter = 0;
  }

  protected void act() {
    if (this.mapObjects.isEmpty()) return;
    if (AI.class.isInstance(this.mapObjects.get(0))) {
      for (T obj : this.mapObjects) {
        ((AI) obj).act();
      }
    }
  }

  protected T createNewMapObject() {
    T obj = supplier.get();
    obj.addToMapObjectGroup(this);
    this.mapObjects.add(obj);
    return obj;
  }

  protected void decrementCounter() {
    counter--;
  }

  protected int getCounter() {
    return counter;
  }

  protected T getMapObject(int index) {
    return mapObjects.get(index);
  }

  protected void incrementCounter() {
    counter++;
  }

  protected int numberOfMapObjects() {
    return mapObjects.size();
  }
}
