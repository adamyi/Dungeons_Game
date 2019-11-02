package unsw.dungeon.gameplay;

public class Boulder extends Terrain {
  public Boulder() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    if (!(Player.class.isInstance(object))) {
      return false;
    }

    // find cell opposite to player
    Cell boulderCell = this.getCell();
    int playerDirection = Direction.UNKNOWN;

    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      if (boulderCell.getAdjacentCell(direction).getMapObjectOfType(Player.class) != null) {
        playerDirection = direction;
      }
    }

    int opposite = Direction.getOppositeDirection(playerDirection);
    return boulderCell.getAdjacentCell(opposite).canWalkInto(opposite, this);
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    this.removeFromCell();
    next.addMapObject(this);
  }
}
