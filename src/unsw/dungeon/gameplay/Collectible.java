package unsw.dungeon.gameplay;

public abstract class Collectible extends MapObject {
  public Collectible() {
    super();
  }

  private Player owner;

  protected void pickup(Player player) {
    this.removeFromCell();
    this.owner = player;
    player.addToInventory(this);
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    this.pickup(player);
  }
}
