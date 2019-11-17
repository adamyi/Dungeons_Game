package unsw.dungeon.gameengine.gameplay;

import unsw.dungeon.gameengine.SharedConstants;
import unsw.dungeon.utils.DirectionUtils;

public class Portal extends Terrain implements Pairable {
  private Portal pair;

  public Portal() {
    super();
  }

  private void movePlayer(Player player, int defaultDirection) {
    Cell portalAdjacentCell = null;
    if (defaultDirection != Direction.UNKNOWN) {
      portalAdjacentCell = pair.getCell().getAdjacentCell(defaultDirection);
      if (portalAdjacentCell.canWalkInto(player)) {
        player.setCell(pair.getCell());
        player.moveTo(portalAdjacentCell);
        return;
      }
    }
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      portalAdjacentCell = pair.getCell().getAdjacentCell(direction);
      if (portalAdjacentCell.canWalkInto(player)) {
        player.setCell(pair.getCell());
        player.moveTo(portalAdjacentCell);
        return;
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
    this.movePlayer(player, DirectionUtils.getDirectionBetweenAdjacentCells(start, this.getCell()));
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
