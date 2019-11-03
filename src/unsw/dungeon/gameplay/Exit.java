package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

public class Exit extends Terrain implements AutonomousObject {
  public Exit() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell direction, Player player) {
    if (this.getState(SharedConstants.EXIT_ENTERED_STATE) == null) {
      this.setState(SharedConstants.EXIT_ENTERED_STATE);
      this.getMapObjectGroup().decrementCounter();
    }
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("X");
  }

  @Override
  public void act() {
    if (this.getCell().getMapObjectOfType(Player.class) == null) {
      if (this.getState(SharedConstants.EXIT_ENTERED_STATE) != null) {
        this.removeState(SharedConstants.EXIT_ENTERED_STATE);
        this.getMapObjectGroup().incrementCounter();
      }
    }
  }
}
