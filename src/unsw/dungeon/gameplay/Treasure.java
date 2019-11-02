package unsw.dungeon.gameplay;

public class Treasure extends Collectible {
  public Treasure() {
    super();
  }

  @Override
  protected void playerInteraction(int direction, Player player) {
    this.pickup(player);
    this.getMapObjectGroup().decrementCounter();
  }
}
