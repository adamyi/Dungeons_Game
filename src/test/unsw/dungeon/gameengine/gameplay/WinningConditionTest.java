package unsw.dungeon.gameengine.gameplay;

import java.util.HashMap;
import org.junit.Test;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.SharedConstants;
import unsw.dungeon.gameengine.objectives.FOLAndObjectiveNode;
import unsw.dungeon.gameengine.objectives.FOLOrObjectiveNode;
import unsw.dungeon.gameengine.objectives.LeafObjectiveNode;

public class WinningConditionTest {
  @Test
  public void victoryByTreasure() {
    Cell playerCell = new Cell(0, 0);
    Cell treasureCell1 = new Cell(0, 1);
    Cell treasureCell2 = new Cell(0, 2);
    Cell treasureCell3 = new Cell(0, 3);

    Player player = new Player();

    // P T T T
    playerCell.setAdjacentCell(Direction.RIGHT, treasureCell1);
    treasureCell1.setAdjacentCell(Direction.LEFT, playerCell);
    treasureCell1.setAdjacentCell(Direction.RIGHT, treasureCell2);
    treasureCell2.setAdjacentCell(Direction.LEFT, treasureCell1);
    treasureCell2.setAdjacentCell(Direction.RIGHT, treasureCell3);
    treasureCell3.setAdjacentCell(Direction.LEFT, treasureCell2);

    MapObjectGroup<Treasure> treasureGroup = new MapObjectGroup<Treasure>(Treasure::new);
    Treasure treasure1 = treasureGroup.createNewMapObject(null);
    Treasure treasure2 = treasureGroup.createNewMapObject(null);
    Treasure treasure3 = treasureGroup.createNewMapObject(null);

    assert (treasureGroup.getCounter() == 3);

    assert (treasure1.getMapObjectGroup() == treasureGroup);
    assert (treasure2.getMapObjectGroup() == treasureGroup);
    assert (treasure3.getMapObjectGroup() == treasureGroup);

    treasure1.moveTo(treasureCell1);
    treasure2.moveTo(treasureCell2);
    treasure3.moveTo(treasureCell3);

    assert (treasure1.getCell() == treasureCell1);
    assert (treasure2.getCell() == treasureCell2);
    assert (treasure3.getCell() == treasureCell3);

    player.moveTo(treasureCell1);
    assert (treasureGroup.getCounter() == 2);
    player.moveTo(treasureCell2);
    assert (treasureGroup.getCounter() == 1);
    player.moveTo(treasureCell3);

    assert (player.hasObjectInInventory(treasure1));
    assert (player.hasObjectInInventory(treasure2));
    assert (player.hasObjectInInventory(treasure3));

    assert (treasureGroup.getCounter() == 0);
  }

  @Test
  public void victoryBySlaughter() {
    Cell playerCell = new Cell(0, 0);
    Cell enemyCell1 = new Cell(0, 1);
    Cell enemyCell2 = new Cell(0, 2);
    Cell enemyCell3 = new Cell(0, 3);

    Player player = new Player();

    // P T T T
    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell1);
    enemyCell1.setAdjacentCell(Direction.LEFT, playerCell);
    enemyCell1.setAdjacentCell(Direction.RIGHT, enemyCell2);
    enemyCell2.setAdjacentCell(Direction.LEFT, enemyCell1);
    enemyCell2.setAdjacentCell(Direction.RIGHT, enemyCell3);
    enemyCell3.setAdjacentCell(Direction.LEFT, enemyCell2);

    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>(Enemy::new);
    Enemy enemy1 = enemyGroup.createNewMapObject(null);
    Enemy enemy2 = enemyGroup.createNewMapObject(null);
    Enemy enemy3 = enemyGroup.createNewMapObject(null);

    assert (enemyGroup.getCounter() == 3);

    assert (enemy1.getMapObjectGroup() == enemyGroup);
    assert (enemy2.getMapObjectGroup() == enemyGroup);
    assert (enemy3.getMapObjectGroup() == enemyGroup);

    enemy1.moveTo(enemyCell1);
    enemy2.moveTo(enemyCell2);
    enemy3.moveTo(enemyCell3);

    assert (enemy1.getCell() == enemyCell1);
    assert (enemy2.getCell() == enemyCell2);
    assert (enemy3.getCell() == enemyCell3);

    Sword sword = new Sword();
    player.addToInventory(sword);

    player.moveTo(enemyCell1);
    assert (enemyGroup.getCounter() == 2);
    player.moveTo(enemyCell2);
    assert (enemyGroup.getCounter() == 1);
    player.moveTo(enemyCell3);

    assert (enemy1.getCell() == null);
    assert (enemy2.getCell() == null);
    assert (enemy3.getCell() == null);

    assert (player.getCell() == enemyCell3);
    assert (enemyGroup.getCounter() == 0);
  }

  @Test
  public void victoryByEscape() {
    Cell playerCell = new Cell(0, 0);
    Cell exitCell = new Cell(0, 1);

    Player player = new Player();

    MapObjectGroup<Exit> exitGroup = new MapObjectGroup<Exit>(Exit::new);
    Exit exit = exitGroup.createNewMapObject(null);

    // P E
    playerCell.setAdjacentCell(Direction.RIGHT, exitCell);
    exitCell.setAdjacentCell(Direction.LEFT, playerCell);

    player.moveTo(playerCell);
    exit.moveTo(exitCell);

    assert (player.getCell() == playerCell);
    assert (exit.getCell() == exitCell);

    exit.act();
    assert (exit.getMapObjectGroup() == exitGroup);
    assert (exit.getState(SharedConstants.EXIT_ENTERED_STATE) == null);
    assert (exitGroup.getCounter() == 1);

    player.moveTo(exitCell);

    exit.act();
    assert (player.getCell() == exitCell);
    assert (exit.getState(SharedConstants.EXIT_ENTERED_STATE) != null);
    assert (exitGroup.getCounter() == 0);
  }

  @Test
  public void victoryByIndianaJones() {
    // P B1 S1 #1 #2 B1 S1 #3
    Cell playerCell = new Cell(0, 0);
    Cell boulderCell1 = new Cell(0, 1);
    Cell floorSwitchCell1 = new Cell(0, 2);
    Cell emptyCell1 = new Cell(0, 3);
    Cell emptyCell2 = new Cell(0, 4);
    Cell boulderCell2 = new Cell(0, 5);
    Cell floorSwitchCell2 = new Cell(0, 6);
    Cell emptyCell3 = new Cell(0, 7);

    playerCell.setAdjacentCell(Direction.RIGHT, boulderCell1);
    boulderCell1.setAdjacentCell(Direction.LEFT, playerCell);
    boulderCell1.setAdjacentCell(Direction.RIGHT, floorSwitchCell1);
    floorSwitchCell1.setAdjacentCell(Direction.LEFT, boulderCell1);
    floorSwitchCell1.setAdjacentCell(Direction.RIGHT, emptyCell1);
    emptyCell1.setAdjacentCell(Direction.LEFT, floorSwitchCell1);
    emptyCell1.setAdjacentCell(Direction.RIGHT, emptyCell2);
    emptyCell2.setAdjacentCell(Direction.LEFT, emptyCell1);
    emptyCell2.setAdjacentCell(Direction.RIGHT, boulderCell2);
    boulderCell2.setAdjacentCell(Direction.LEFT, emptyCell2);
    boulderCell2.setAdjacentCell(Direction.RIGHT, floorSwitchCell2);
    floorSwitchCell2.setAdjacentCell(Direction.LEFT, boulderCell2);
    floorSwitchCell2.setAdjacentCell(Direction.RIGHT, emptyCell3);
    emptyCell3.setAdjacentCell(Direction.LEFT, floorSwitchCell2);

    boulderCell1.setAdjacentCell(Direction.UP, emptyCell1);
    boulderCell1.setAdjacentCell(Direction.DOWN, emptyCell1);
    boulderCell2.setAdjacentCell(Direction.UP, emptyCell3);
    boulderCell2.setAdjacentCell(Direction.DOWN, emptyCell3);

    Player player = new Player();
    Boulder boulder1 = new Boulder();
    Boulder boulder2 = new Boulder();

    MapObjectGroup<FloorSwitch> floorSwitchGroup =
        new MapObjectGroup<FloorSwitch>(FloorSwitch::new);
    FloorSwitch floorSwitch1 = floorSwitchGroup.createNewMapObject(null);
    FloorSwitch floorSwitch2 = floorSwitchGroup.createNewMapObject(null);

    player.moveTo(playerCell);
    boulder1.moveTo(boulderCell1);
    boulder2.moveTo(boulderCell2);
    floorSwitch1.moveTo(floorSwitchCell1);
    floorSwitch2.moveTo(floorSwitchCell2);

    floorSwitch1.act();
    floorSwitch2.act();

    assert (floorSwitchGroup.getCounter() == 2);
    assert (floorSwitch1.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) == null);
    assert (floorSwitch2.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) == null);

    player.moveTo(boulderCell1);

    assert (player.getCell() == boulderCell1);
    assert (boulder1.getCell() == floorSwitchCell1);
    assert (floorSwitch1.getCell() == floorSwitchCell1);

    floorSwitch1.act();
    floorSwitch2.act();

    assert (floorSwitchGroup.getCounter() == 1);
    assert (floorSwitch1.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) != null);
    assert (floorSwitch2.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) == null);

    player.moveTo(emptyCell2);

    assert (player.getCell() == emptyCell2);
    assert (boulder2.getCell() == boulderCell2);
    assert (floorSwitch2.getCell() == floorSwitchCell2);

    player.moveTo(boulderCell2);

    assert (player.getCell() == boulderCell2);
    assert (boulder2.getCell() == floorSwitchCell2);
    assert (floorSwitch2.getCell() == floorSwitchCell2);

    floorSwitch1.act();
    floorSwitch2.act();

    assert (floorSwitchGroup.getCounter() == 0);
    assert (floorSwitch1.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) != null);
    assert (floorSwitch2.getState(SharedConstants.FLOORSWITCH_ACTIVE_STATE) != null);
  }

  @Test
  public void compoundInterest() {
    MapObjectGroup<Treasure> treasureGroup = new MapObjectGroup<Treasure>(Treasure::new);
    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>(Enemy::new);

    FOLAndObjectiveNode andNode = new FOLAndObjectiveNode();
    FOLOrObjectiveNode orNode = new FOLOrObjectiveNode();

    LeafObjectiveNode a = new LeafObjectiveNode(Treasure.class);
    LeafObjectiveNode b = new LeafObjectiveNode(Enemy.class);

    andNode.addChild(a);
    andNode.addChild(b);

    orNode.addChild(a);
    orNode.addChild(b);

    HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups = new HashMap();
    mapObjectGroups.put(Treasure.class, treasureGroup);
    mapObjectGroups.put(Enemy.class, enemyGroup);

    assert (andNode.hasWon(mapObjectGroups) == true);
    assert (orNode.hasWon(mapObjectGroups) == true);

    treasureGroup.incrementCounter();

    assert (andNode.hasWon(mapObjectGroups) == false);
    assert (orNode.hasWon(mapObjectGroups) == true);

    enemyGroup.incrementCounter();

    assert (andNode.hasWon(mapObjectGroups) == false);
    assert (orNode.hasWon(mapObjectGroups) == false);

    treasureGroup.decrementCounter();

    assert (andNode.hasWon(mapObjectGroups) == false);
    assert (orNode.hasWon(mapObjectGroups) == true);
  }
}
