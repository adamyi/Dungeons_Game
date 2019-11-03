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
  protected void playerInteraction(Cell start, Player player) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("W");
  }
}
