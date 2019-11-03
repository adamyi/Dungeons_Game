package unsw.dungeon.gameplay;

public class Treasure extends Collectible {
  public Treasure() {
    super();
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    this.pickup(player);
    this.getMapObjectGroup().decrementCounter();
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("T");
  }
}
