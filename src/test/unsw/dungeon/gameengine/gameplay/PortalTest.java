package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;

public class PortalTest {
  @Test
  public void entityCanWalkIntoPortal() {
    Cell playerCell = new Cell(0, 0);
    Cell portalCell1 = new Cell(0, 1);
    Cell portalCell2 = new Cell(0, 2);
    Cell emptyCell = new Cell(0, 3);
    Player player = new Player();
    Portal portal1 = new Portal();
    Portal portal2 = new Portal();

    portal1.setPair(portal2);
    portal2.setPair(portal1);

    playerCell.setAdjacentCell(Direction.RIGHT, portalCell1);
    portalCell1.setAdjacentCell(Direction.LEFT, playerCell);

    portalCell2.setAdjacentCell(Direction.UP, emptyCell);
    portalCell2.setAdjacentCell(Direction.DOWN, emptyCell);
    portalCell2.setAdjacentCell(Direction.LEFT, emptyCell);
    portalCell2.setAdjacentCell(Direction.RIGHT, emptyCell);

    portal1.setPair(portal2);
    portal2.setPair(portal1);

    player.moveTo(playerCell);
    assert (player.getCell() == playerCell);
    portal1.moveTo(portalCell1);
    assert (portal1.getCell() == portalCell1);
    portal2.moveTo(portalCell2);
    assert (portal2.getCell() == portalCell2);

    player.moveTo(portalCell1);
    assert (player.getCell() == emptyCell);
    assert (portal1.getCell() == portalCell1);
    assert (portal2.getCell() == portalCell2);
  }
}
