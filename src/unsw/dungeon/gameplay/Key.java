package unsw.dungeon.gameplay;

public class Key extends Collectible {
  private Door pair;

  public Key(Door door) {
    super();

    if (door == null) {
      throw new IllegalArgumentException();
    }

    pair = door;
  }
}
