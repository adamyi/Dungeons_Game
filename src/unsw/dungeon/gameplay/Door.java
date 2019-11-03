package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

public class Door extends Terrain implements Pairable {
  private Key pair;

  public Door() {
    super();
  }

  public Pairable getPair() {
    return pair;
  }

  public void setPair(Pairable pair) {
    if (!Key.class.isInstance(pair)) {
      throw new IllegalArgumentException();
    }
    this.pair = (Key) pair;
  }

  public String getPairType() {
    return SharedConstants.DOOR_KEY_PAIR;
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    if (this.getState(SharedConstants.DOOR_OPEN_STATE) != null) {
      return true;
    }
    if (Player.class.isInstance(object)) {
      if (Key.class.isInstance(pair) && ((Player) object).hasObjectInInventory(pair)) return true;
    }
    return false;
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    if (this.getState(SharedConstants.DOOR_OPEN_STATE) == null) {
      if (Key.class.isInstance(pair) && (player.hasObjectInInventory(pair))) {
        this.setState(SharedConstants.DOOR_OPEN_STATE);

        player.removeFromInventory(pair);
      }
    }
  }

  @Override
  protected StringBuilder printCLI() {
    if (this.getState(SharedConstants.DOOR_OPEN_STATE) == null) return new StringBuilder("D");
    return new StringBuilder("d");
  }
}
