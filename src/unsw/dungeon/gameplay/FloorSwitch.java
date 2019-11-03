package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

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
      if (this.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) == null) {
        this.setState(SharedConstants.FLOORSWITCH_ACTIVE_STATE);
        this.getMapObjectGroup().decrementCounter();
      }
    } else {
      if (this.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) != null) {
        this.removeState(SharedConstants.FLOORSWITCH_ACTIVE_STATE);
        this.getMapObjectGroup().incrementCounter();
      }
    }
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {}

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("S");
  }
}
