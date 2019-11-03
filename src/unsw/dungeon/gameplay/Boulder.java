package unsw.dungeon.gameplay;

import unsw.dungeon.utils.DirectionUtils;

public class Boulder extends Terrain {
  public Boulder() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
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

    int opposite = DirectionUtils.getOppositeDirection(playerDirection);
    return boulderCell.getAdjacentCell(opposite).canWalkInto(this);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    this.getCell()
        .getAdjacentCell(DirectionUtils.getDirectionBetweenAdjacentCells(start, this.getCell()))
        .addMapObject(this);
    this.removeFromCell();
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("B");
  }
}
