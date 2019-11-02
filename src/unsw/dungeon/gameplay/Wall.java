package unsw.dungeon.gameplay;

public class Wall extends Terrain {
  public Wall() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return false;
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    throw new UnsupportedOperationException();
  }
}
