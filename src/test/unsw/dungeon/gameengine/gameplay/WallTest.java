package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;

public class WallTest {
  @Test
  public void walkIntoWalls() {
    Cell playerCell = new Cell(0, 0);
    Cell enemyCell = new Cell(0, 1);
    Cell wallCell = new Cell(0, 2);

    Player player = new Player();
    Enemy enemy = new Enemy();
    Wall wall = new Wall();

    player.moveTo(playerCell);
    enemy.moveTo(enemyCell);
    wall.moveTo(wallCell);

    // P W E
    playerCell.setAdjacentCell(Direction.RIGHT, wallCell);
    wallCell.setAdjacentCell(Direction.LEFT, playerCell);
    wallCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.LEFT, wallCell);

    assert (player.getCell() == playerCell);
    assert (enemy.getCell() == enemyCell);
    assert (wall.getCell() == wallCell);

    player.moveTo(wallCell);
    enemy.moveTo(wallCell);

    assert (player.getCell() == playerCell);
    assert (enemy.getCell() == enemyCell);
    assert (wall.getCell() == wallCell);
  }
}
