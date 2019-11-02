package unsw.dungeon.gameplay;

public class Door extends Terrain {
  private Key pair;
  
  public Door(Key key) {
    super();
    
    if (key == null) {
      throw new IllegalArgumentException();
    }
    
    pair = key;
  }
  
  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    if (this.getState("OPEN") != null) {
      return true;
    }
    return false;
  }
  
  @Override
  protected void playerInteraction(Cell next, Player player) {
    if (this.getState("CLOSED") != null) {
      if (player.hasObjectInInventory(pair)) {
        this.removeState("CLOSED");
        
        MapObjectState open = new MapObjectState("OPEN");
        this.setState(open);
        
        player.removeFromInventory(pair);
      }
    }
  }
}