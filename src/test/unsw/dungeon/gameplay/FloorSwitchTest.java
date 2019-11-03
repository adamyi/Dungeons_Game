package unsw.dungeon.gameplay;

import org.junit.Test;

import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.Boulder;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.FloorSwitch;
import unsw.dungeon.gameplay.Player;

public class FloorSwitchTest {
  @Test
  public void entityCanWalkIntoFloorSwitch() {
    Cell playerCell = new Cell(0);
    Cell boulderCell = new Cell(1);
    Cell floorSwitchCell = new Cell(2);
    Cell emptyCell = new Cell(3);
    Player player = new Player();
    MapObjectGroup<FloorSwitch> floorSwitchGroup = new MapObjectGroup<FloorSwitch>(FloorSwitch::new);
    FloorSwitch floorSwitch = floorSwitchGroup.createNewMapObject(null);
    floorSwitch.addToMapObjectGroup(floorSwitchGroup);

    MapObjectGroup<Boulder> boulderGroup = new MapObjectGroup<Boulder>(Boulder::new);
    Boulder boulder = boulderGroup.createNewMapObject(null);
    boulder.addToMapObjectGroup(boulderGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, boulderCell);
    boulderCell.setAdjacentCell(Direction.LEFT, playerCell);
    boulderCell.setAdjacentCell(Direction.RIGHT, floorSwitchCell);
    floorSwitchCell.setAdjacentCell(Direction.LEFT, boulderCell);

    boulderCell.setAdjacentCell(Direction.UP, emptyCell);
    boulderCell.setAdjacentCell(Direction.DOWN, emptyCell);

    player.moveTo(playerCell);
    assert (player.getCell() == playerCell);
    boulder.moveTo(boulderCell);
    assert (boulder.getCell() == boulderCell);
    floorSwitch.moveTo(floorSwitchCell);
    assert (floorSwitch.getCell() == floorSwitchCell);

    player.moveTo(boulderCell);
    assert (player.getCell() == boulderCell);
    assert (boulder.getCell() == floorSwitchCell);
    assert (floorSwitch.getCell() == floorSwitchCell);

    for (int i = 0; i < floorSwitchCell.getNumberOfMapObjects(); i++) {
      System.err.println(floorSwitchCell.getMapObjectAtIndex(i));
    }

    assert (floorSwitch.getState("floorswitch_active_state") == null);
    floorSwitch.act();
    assert (floorSwitch.getState("floorswitch_active_state") != null);
  }

  
}