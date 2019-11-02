package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

public class Potion extends Collectible {
  public Potion() {
    super();
  }

  protected void use() {
    this.getOwner().setState(SharedConstants.PLAYER_INVINCIBLE_STATE, 10);
    this.getOwner().removeFromInventory(this);
  }
}
