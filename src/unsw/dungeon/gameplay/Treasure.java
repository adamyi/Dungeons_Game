package unsw.dungeon.gameplay;

public class Treasure extends Collectible {
  public Treasure() {
    super();
  }
  
  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("T");
  }
}
