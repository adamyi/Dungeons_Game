package unsw.dungeon.gameengine.gameplay;

import unsw.dungeon.gameengine.SharedConstants;

public class Potion extends Collectible {
  public Potion() {
    super();
  }

  protected void use() {
    this.getOwner()
        .setState(
            SharedConstants.PLAYER_INVINCIBLE_STATE, SharedConstants.PLAYER_INVINCIBLE_DURATION);
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
