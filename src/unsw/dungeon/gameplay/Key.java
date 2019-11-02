package unsw.dungeon.gameplay;

public class Key extends Collectible implements Pairable {
  private Pairable pair;

  public Key(Door door) {
    super();

    if (door == null) {
      throw new IllegalArgumentException();
    }

    this.setPair(pair);
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
    this.pair = pair;
  }

  @Override
  public String getPairType() {
    return "Door";
  }
}
