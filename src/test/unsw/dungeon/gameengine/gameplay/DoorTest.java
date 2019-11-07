package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;
import unsw.dungeon.gameengine.MapObjectGroup;

public class DoorTest {

  @Test
  public void lockedTest() {
    Cell playerCell = new Cell(0, 0);
    Cell doorCell = new Cell(0, 1);
    Player player = new Player();
    MapObjectGroup<Door> doorGroup = new MapObjectGroup<Door>(Door::new);
    Door door = doorGroup.createNewMapObject(null);
    door.addToMapObjectGroup(doorGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, doorCell);
    doorCell.setAdjacentCell(Direction.LEFT, playerCell);

    player.moveTo(playerCell);
    assert (player.getCell() == playerCell);
    door.moveTo(doorCell);
    assert (door.getCell() == doorCell);

    Key key = new Key();
    key.setPair(door);
    door.setPair(key);

    // move player to door
    player.moveTo(doorCell);
    System.err.println(playerCell);
    System.err.println(doorCell);
    System.err.println(player.getCell());

    // door is impassable to all objects
    System.err.println(door.canWalkInto(player));
    System.err.println(doorCell.canWalkInto(player));

    // player fails without key
    assert (player.getCell() == playerCell);
    assert (door.getCell() == doorCell);

    // player acquires key
    System.err.println(key.getCell());
    player.addToInventory(key);
    assert (player.hasObjectInInventory(key));

    // player enters door
    player.moveTo(doorCell);
    assert (player.getCell() == doorCell);
    assert (door.getState("door_open_state") != null);
  }
}
