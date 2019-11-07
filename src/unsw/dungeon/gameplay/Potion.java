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

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("I");
  }

  @Override
  public String initialImage() {
    return "brilliant_blue_new.png";
  }
}
