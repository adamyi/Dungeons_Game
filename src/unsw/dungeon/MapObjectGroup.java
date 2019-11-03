package unsw.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import unsw.dungeon.gameplay.AI;
import unsw.dungeon.gameplay.MapObject;

public class MapObjectGroup<T extends MapObject> implements Subject {
  private Supplier<T> supplier;
  private int counter;
  private List<T> mapObjects;
  private List<Observer> observers;

  public MapObjectGroup(Supplier<T> supplier) {
    this.supplier = supplier;
    this.mapObjects = new ArrayList<T>();
    this.observers = new ArrayList<>();
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

  protected T createNewMapObject(HashMap<String, Object> properties) {
    T obj = supplier.get();
    obj.addToMapObjectGroup(this);
    obj.initProperties(properties);
    this.mapObjects.add(obj);
    counter++;
    return obj;
  }

  public void decrementCounter() {
    counter--;
    if (counter == 0) {
      notifyObservers();
    }
  }

  public int getCounter() {
    return counter;
  }

  protected T getMapObject(int index) {
    return mapObjects.get(index);
  }

  protected T getMapObject() {
    return this.getMapObject(0);
  }

  public void incrementCounter() {
    counter++;
  }

  protected int numberOfMapObjects() {
    return mapObjects.size();
  }

  @Override
  public void attach(Observer observer) {
    if (!this.observers.contains(observer)) this.observers.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    this.observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : this.observers) {
      observer.update(this);
    }
  }
}
