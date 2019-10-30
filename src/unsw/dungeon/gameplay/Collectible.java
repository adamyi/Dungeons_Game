package unsw.dungeon.gameplay;

import unsw.dungeon.MapObjectGroup;

public abstract class Collectible extends MapObject {
  public Collectible(MapObjectGroup group) {
    super(group);
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
