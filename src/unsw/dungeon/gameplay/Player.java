package unsw.dungeon.gameplay;

import java.util.ArrayList;
import unsw.dungeon.GameOverException;

public class Player extends Entity {
  ArrayList<Collectible> inventory;

  public Player() {
    super();
    this.inventory = new ArrayList<>();
    // this.setState(SharedConstants.PLAYER_INVINCIBLE_STATE, Integer.MAX_VALUE);
  }

  protected void die() throws GameOverException {
    throw new GameOverException(false);
  }

  protected boolean hasObjectInInventory(Collectible object) {
    return inventory.contains(object);
  }

  protected void addToInventory(Collectible object) {
    inventory.add(object);
  }

  protected Collectible getCollectibleOfTypeInInventory(Class<? extends Collectible> type) {
    for (Collectible item : inventory) {
      if (type.isInstance(item)) return item;
    }
    return null;
  }

  protected void removeFromInventory(Collectible object) {
    inventory.remove(object);
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return object.canWalkInto(this);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {}

  @Override
  protected void moveTo(int direction, Cell next) {
    super.moveTo(direction, next);
    next.playerInteraction(this.getCell(), this);
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("P");
  }
}
