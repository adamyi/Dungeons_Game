package unsw.dungeon.gameplay;

public class Portal extends Terrain {
  private Portal pair;

  public Portal(Portal portal) {
    super();

    if (portal == null || portal == this) {
      throw new IllegalArgumentException();
    }

    pair = portal;
  }

  private void movePlayer(Player player) {
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      if (pair.getCell().getAdjacentCell(direction).canWalkInto(direction, player)) {
        pair.getCell().getAdjacentCell(direction).addMapObject(player);
      }
    }
    player.removeFromCell();
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    this.movePlayer(player);
  }
}
