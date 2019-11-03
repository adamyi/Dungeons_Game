package unsw.dungeon.gameplay;

import org.junit.Test;

import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.SharedConstants;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.Enemy;
import unsw.dungeon.gameplay.Key;
import unsw.dungeon.gameplay.Player;
import unsw.dungeon.gameplay.Potion;
import unsw.dungeon.gameplay.Sword;
import unsw.dungeon.gameplay.Treasure;
import unsw.dungeon.gameplay.Wall;

public class WallTest {
  @Test
  public void walkIntoWalls() {
    Cell playerCell = new Cell(0);
    Cell enemyCell = new Cell(1);
    Cell wallCell = new Cell(2);

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