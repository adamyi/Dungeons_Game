package unsw.dungeon.gameplay;

import org.junit.Test;

import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.Collectible;
import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.Key;
import unsw.dungeon.gameplay.Player;
import unsw.dungeon.gameplay.Potion;
import unsw.dungeon.gameplay.Sword;
import unsw.dungeon.gameplay.Treasure;

public class CollectiblesAddedToInventoryTest {
  @Test
  public void addCollectiblesToInventory() {
    Cell playerCell = new Cell(0);
    Cell keyCell = new Cell(1);
    Cell potionCell = new Cell(2);
    Cell swordCell = new Cell(3);
    Cell treasureCell = new Cell(4);
    Player player = new Player();
    
    MapObjectGroup<Key> keyGroup = new MapObjectGroup<Key>(Key::new);
    Key key = new Key();
    key.addToMapObjectGroup(keyGroup);

    MapObjectGroup<Potion> potionGroup = new MapObjectGroup<Potion>(Potion::new);
    Potion potion = new Potion();
    potion.addToMapObjectGroup(potionGroup);

    MapObjectGroup<Sword> swordGroup = new MapObjectGroup<Sword>(Sword::new);
    Sword sword = new Sword();
    sword.addToMapObjectGroup(swordGroup);

    MapObjectGroup<Treasure> treasureGroup = new MapObjectGroup<Treasure>(Treasure::new);
    Treasure treasure = new Treasure();
    treasure.addToMapObjectGroup(treasureGroup);

    player.moveTo(playerCell);
    key.moveTo(keyCell);
    potion.moveTo(potionCell);
    sword.moveTo(swordCell);
    treasure.moveTo(treasureCell);

    assert (player.getCell() == playerCell);
    assert (key.getCell() == keyCell);
    assert (potion.getCell() == potionCell);
    assert (sword.getCell() == swordCell);
    assert (treasure.getCell() == treasureCell);

    player.moveTo(keyCell);
    assert (player.getCell() == keyCell);
    assert (player.hasObjectInInventory(key));

    player.moveTo(potionCell);
    assert (player.getCell() == potionCell);
    assert (player.hasObjectInInventory(potion));

    player.moveTo(swordCell);
    assert (player.getCell() == swordCell);
    assert (player.hasObjectInInventory(sword));

    player.moveTo(treasureCell);
    assert (player.getCell() == treasureCell);
    assert (player.hasObjectInInventory(treasure));
  }
}