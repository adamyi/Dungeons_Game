package unsw.dungeon.gameplay;

public class Exit extends Terrain {
  public Exit() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell direction, Player player) {
    // TODO: check if objectives satisfied then allow player through then throw Exception GameWon()
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("X");
  }
}
