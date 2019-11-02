package unsw.dungeon.gameplay;

public class Treasure extends Collectible {
  public Treasure() {
    super();
  }
  
  @Override
  protected void playerInteraction(Cell next, Player player) {
    this.pickup(player);
    this.group.decrementCounter();
  }
}