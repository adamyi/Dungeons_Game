package unsw.dungeon.gameplay;

public class Boulder extends Terrain {
  public Boulder() {
    super();
  }
  
  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    if (!object.isInstance(player)) {
      return false;
    }
    
    // find cell opposite to player
    Cell boulderCell = this.getCell();
    int playerDirection = Direction.UNKNOWN;
    int directions[] = {Direction.TOP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
    
    for (int direction : directions) {
      if (boulderCell.getAdjacentCell(direction).getMapObjectOfType(Player) != null) {
        playerDirection = direction;
      }
    }
    
    int opposite = Direction.getOppositeDirection(playerDirection);
    if (boulderCell.getAdjacentCell(opposite).canWalkInto(object, next)) {
      return true;
    }
    
    return false;
  }
  
  
  @Override
  protected void playerInteraction(Cell next, Player player) {
    this.removeFromCell();
    next.addMapObject(this);
  }
}