package unsw.dungeon.gameengine.gameplay;

import unsw.dungeon.gameengine.SharedConstants;

public class Portal extends Terrain implements Pairable {
  private Portal pair;

  public Portal() {
    super();
  }

  private void movePlayer(Player player) {
    Cell portalAdjacentCell = null;
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      portalAdjacentCell = pair.getCell().getAdjacentCell(direction);
      if (portalAdjacentCell.canWalkInto(player)) {
        player.moveTo(portalAdjacentCell);
      }
    }
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
    if (pair == this) {
      throw new IllegalArgumentException();
    }
    this.pair = (Portal) pair;
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

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("O");
  }

  @Override
  public String initialImage() {
    return "portal.png";
  }
}
