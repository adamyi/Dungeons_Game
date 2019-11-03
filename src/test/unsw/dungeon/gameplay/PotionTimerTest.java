package unsw.dungeon.gameplay;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import unsw.dungeon.MapObjectGroup;
import unsw.dungeon.SharedConstants;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.Collectible;
import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.Player;
import unsw.dungeon.gameplay.Potion;

public class PotionTimerTest {
  @Test
  public void potionTimerTest() {
    Cell playerCell = new Cell(0);
    Cell potionCell = new Cell(1);
    Player player = new Player();
    
    MapObjectGroup<Potion> potionGroup = new MapObjectGroup<Potion>(Potion::new);
    Potion potion = potionGroup.createNewMapObject(null);
    potion.addToMapObjectGroup(potionGroup);

    player.moveTo(playerCell);
    potion.moveTo(potionCell);

    assert (player.getCell() == playerCell);
    assert (potion.getCell() == potionCell);

    player.moveTo(potionCell);

    assert (player.getCell() == potionCell);
    assert (player.hasObjectInInventory(potion));

    LocalDateTime start = LocalDateTime.now();
    potion.use();
    while (player.getState(SharedConstants.PLAYER_INVINCIBLE_STATE) != null) {
      //Thread.sleep(500);
    }
    LocalDateTime end = LocalDateTime.now();

    long period = Duration.between(start, end).getSeconds();
    System.out.println(period);
  }
}