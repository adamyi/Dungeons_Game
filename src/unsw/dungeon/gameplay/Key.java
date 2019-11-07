package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

public class Key extends Collectible implements Pairable {
  private Door pair;

  public Key() {
    super();
  }

  @Override
  public Pairable getPair() {
    return pair;
  }

  @Override
  public void setPair(Pairable pair) {
    if (!Door.class.isInstance(pair)) {
      throw new IllegalArgumentException();
    }
    this.pair = (Door) pair;
  }

  @Override
  public String getPairType() {
    return SharedConstants.DOOR_KEY_PAIR;
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("K");
  }

  @Override
  public String initialImage() {
    return "key.png";
  }
}
