package unsw.dungeon.gameplay;

public class Wall extends Terrain {
  public Wall() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return false;
  }

  @Override
  protected void playerInteraction(int direction, Player player) {
    throw new UnsupportedOperationException();
  }
}
