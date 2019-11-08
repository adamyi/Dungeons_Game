package unsw.dungeon.gameengine.gameplay;

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

  @Override
  public String initialImage() {
    return "brick_brown_0.png";
  }
}
