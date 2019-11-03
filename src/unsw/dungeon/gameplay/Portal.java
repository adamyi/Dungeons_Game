package unsw.dungeon.gameplay;

import unsw.dungeon.SharedConstants;

public class Portal extends Terrain implements Pairable {
  private Portal pair;

  public Portal(Portal portal) {
    super();

    if (portal == null || portal == this) {
      throw new IllegalArgumentException();
    }

    this.setPair(portal);
  }

  private void movePlayer(Player player) {
    Cell portalAdjacentCell = null;
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      portalAdjacentCell = pair.getCell().getAdjacentCell(direction);
      if (portalAdjacentCell.canWalkInto(direction, player)) {
        portalAdjacentCell.addMapObject(player);
      }
    }
    player.removeFromCell();
  }

  @Override
  public Pairable getPair() {
    return pair;
  }

  @Override
  public void setPair(Pairable pair) {
    if (!Portal.class.isInstance(pair)) {
      throw new IllegalArgumentException();
    }
    this.pair = (Portal)pair;
  }

  @Override
  public String getPairType() {
    return SharedConstants.PORTAL_PORTAL_PAIR;
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    this.movePlayer(player);
  }
}
