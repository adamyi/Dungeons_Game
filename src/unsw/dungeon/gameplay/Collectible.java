package unsw.dungeon.gameplay;

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
    this.removeFromCell();
    this.owner = player;
    player.addToInventory(this);
  }

  @Override
  protected boolean canWalkInto(MapObject object, Cell next) {
    return true;
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    if ((!singleOnly) || owner.getCollectibleOfTypeInInventory(this.getClass()) == null) {
      this.pickup(player);
    }
  }
}
