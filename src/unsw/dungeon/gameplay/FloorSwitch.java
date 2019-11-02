package unsw.dungeon.gameplay;

public class FloorSwitch extends Terrain implements AI {
  public FloorSwitch() {
    super();
  }

  @Override
  public void act() {
    // Class activators[] = {Player.class, Enemy.class, Boulder.class};
    Class activators[] = {Boulder.class};

    boolean isActive = false;
    for (Class activator : activators) {
      if (this.getCell().getMapObjectOfType(activator) != null) {
        isActive = true;
        break;
      }
    }

    if (isActive) {
      if (this.getState("ACTIVE") == null) {
        this.setState("ACTIVE");
        this.getMapObjectGroup().decrementCounter();
      }
    } else {
      if (this.getState("ACTIVE") != null) {
        this.removeState("ACTIVE");
        this.getMapObjectGroup().incrementCounter();
      }
    }
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {}
}
