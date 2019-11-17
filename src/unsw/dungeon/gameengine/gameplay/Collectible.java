package unsw.dungeon.gameengine.gameplay;

public abstract class Collectible extends MapObject {
  private boolean singleOnly;

  public Collectible() {
    super();
    singleOnly = false;
  }

  protected void leaveAloneForeverWithCats() {
    singleOnly = true;
  }

  protected boolean canHaveExtra() {
    return !singleOnly;
  }

  private Player owner;

  protected Player getOwner() {
    return owner;
  }

  protected void pickup(Player player) {
    // this.removeFromCell();
    this.owner = player;
    this.getMapObjectGroup().decrementCounter();
    player.addToInventory(this);
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    if ((!singleOnly) || player.getCollectibleOfTypeInInventory(this.getClass()) == null) {
      this.pickup(player);
    }
  }

  @Override
  public double viewOrder() {
    return 100;
  }
}
