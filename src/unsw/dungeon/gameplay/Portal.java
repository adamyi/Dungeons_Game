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
    player.removeFromCell();
    pair.getCell()
        .getAdjacentCell(Direction.DOWN)
        .addMapObject(player); // can use Math.random to randomize teleport position
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
