package unsw.dungeon.gameplay;

import java.util.ArrayList;
import unsw.dungeon.GameOverException;
import unsw.dungeon.MapObjectGroup;

public class Player extends Entity {
  ArrayList<Collectible> inventory;

  protected Player(MapObjectGroup group) {
    super(group);
    this.inventory = new ArrayList<>();
  }

  protected void die() throws GameOverException {
    throw new GameOverException();
  }

  protected boolean hasObjectInInventory(Collectible object) {
    return inventory.contains(object);
  }

  protected void addToInventory(Collectible object) {
    inventory.add(object);
  }

  protected Collectible getObjectOfType(Class type) {
    for (Collectible item : inventory) {
      if (type.isInstance(item)) return item;
    }
    return null;
  }

  protected void removeFromInventory(Collectible object) {
    inventory.remove(object);
  }

  @Override
  protected boolean canWalkInto(Entity entity, Cell next) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void moveTo(int direction, Cell next) {
    cell.removeMapObject(this);
    cell = next;
    next.addMapObject(this);

    next.playerInteraction(direction, this);
  }
}
