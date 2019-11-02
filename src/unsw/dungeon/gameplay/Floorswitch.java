package unsw.dungeon.gameplay;

public class FloorSwitch extends Terrain implements AI {
  public FloorSwitch() {
    super();
  }
  
  public void act() {
    // TODO: implement stepping on and off
  }
  
  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return true;
  }
  
  @Override
  protected void playerInteraction(Cell next, Player player) {
    
  }
}