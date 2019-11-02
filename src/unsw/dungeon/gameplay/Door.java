package unsw.dungeon.gameplay;

public class Door extends Terrain {
  private Key pair;

  public Door(Key key) {
    super();

    if (key == null) {
      throw new IllegalArgumentException();
    }

    pair = key;
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    if (this.getState("OPEN") != null) {
      return true;
    }
    if (Player.class.isInstance(object)) {
      if (((Player) object).hasObjectInInventory(pair)) return true;
    }
    return false;
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    if (this.getState("OPEN") == null) {
      if (player.hasObjectInInventory(pair)) {
        this.setState("OPEN");

        player.removeFromInventory(pair);
      }
    }
  }
}
