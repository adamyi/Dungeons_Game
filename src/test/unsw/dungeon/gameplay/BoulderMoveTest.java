package unsw.dungeon.gameplay;

import org.junit.Test;

import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.Boulder;
import unsw.dungeon.gameplay.Player;

public class BoulderMoveTest {
  @Test
  public void playerMoveBoulder() {
    Cell playerCell = new Cell(0);
    Cell boulderCell = new Cell(1);
    Cell emptyCellRight = new Cell(2);
    Cell otherDirectionCell= new Cell(3);
    Player player = new Player();
    MapObjectGroup<Boulder> boulderGroup = new MapObjectGroup<Boulder>(Boulder::new);
    Boulder boulder = boulderGroup.createNewMapObject(null);
    boulder.addToMapObjectGroup(boulderGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, boulderCell);
    boulderCell.setAdjacentCell(Direction.LEFT, playerCell);
    boulderCell.setAdjacentCell(Direction.RIGHT, emptyCellRight);
    emptyCellRight.setAdjacentCell(Direction.LEFT, boulderCell);

    boulderCell.setAdjacentCell(Direction.UP, otherDirectionCell);
    boulderCell.setAdjacentCell(Direction.DOWN, otherDirectionCell);

    player.moveTo(playerCell);
    assert (player.getCell() == playerCell);
    boulder.moveTo(boulderCell);
    assert (boulder.getCell() == boulderCell);

    player.moveTo(boulderCell);
    assert (player.getCell() == boulderCell);
    assert (boulder.getCell() == emptyCellRight);
    assert (emptyCellRight.getNumberOfMapObjects() == 1);


  }

  // cannot move if 2 boulder are in a line
  @Test 
  public void playerCannotMoveBoulder() {
    Cell playerCell = new Cell(0);
    Cell boulderCell1 = new Cell(1);
    Cell boulderCell2 = new Cell(2);
    Cell otherDirectionCell1 = new Cell(3);
    Cell otherDirectionCell2 = new Cell(4);
    Player player = new Player();
    MapObjectGroup<Boulder> boulderGroup = new MapObjectGroup<Boulder>(Boulder::new);
    Boulder boulder1 = boulderGroup.createNewMapObject(null);
    Boulder boulder2 = boulderGroup.createNewMapObject(null);
    boulder1.addToMapObjectGroup(boulderGroup);
    boulder2.addToMapObjectGroup(boulderGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, boulderCell1);
    boulderCell1.setAdjacentCell(Direction.LEFT, playerCell);
    boulderCell1.setAdjacentCell(Direction.RIGHT, boulderCell2);
    boulderCell2.setAdjacentCell(Direction.LEFT, boulderCell2);

    boulderCell1.setAdjacentCell(Direction.UP, otherDirectionCell1);
    boulderCell1.setAdjacentCell(Direction.DOWN, otherDirectionCell1);

    boulderCell2.setAdjacentCell(Direction.UP, otherDirectionCell2);
    boulderCell2.setAdjacentCell(Direction.DOWN, otherDirectionCell2);
    boulderCell2.setAdjacentCell(Direction.RIGHT, otherDirectionCell2);

    // P B B
    player.moveTo(playerCell);
    assert (player.getCell() == playerCell);
    boulder1.moveTo(boulderCell1);
    assert (boulder1.getCell() == boulderCell1);
    boulder2.moveTo(boulderCell2);
    assert (boulder2.getCell() == boulderCell2);

    player.moveTo(boulderCell1);
    assert (player.getCell() == playerCell);
    assert (boulder1.getCell() == boulderCell1);
    assert (boulder2.getCell() == boulderCell2);

  }
}