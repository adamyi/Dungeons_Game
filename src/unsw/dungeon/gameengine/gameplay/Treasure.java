package unsw.dungeon.gameengine.gameplay;

public class Treasure extends Collectible {
  public Treasure() {
    super();
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("T");
  }

  @Override
  public String initialImage() {
    return "gold_pile.png";
  }
}
