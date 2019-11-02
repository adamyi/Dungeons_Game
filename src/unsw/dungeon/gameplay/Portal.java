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
    Cell portalAdjacentCell = null;
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      portalAdjacentCell = pair.getCell().getAdjacentCell(direction);
      if (portalAdjacentCell.canWalkInto(direction, player)) {
        portalAdjacentCell.addMapObject(player);
      }
    }
    player.removeFromCell();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(int direction, Player player) {
    this.movePlayer(player);
  }
}
