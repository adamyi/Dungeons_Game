package unsw.dungeon.gameplay;

public class Exit extends Terrain {
  public Exit() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    // TODO: check if objectives satisfied then allow player through then throw Exception GameWon()
  }
}
