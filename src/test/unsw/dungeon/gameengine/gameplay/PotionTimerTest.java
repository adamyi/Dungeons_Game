package unsw.dungeon.gameengine.gameplay;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.Test;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.SharedConstants;

public class PotionTimerTest {
  @Test
  public void potionTimerTest() {
    Cell playerCell = new Cell(0, 0);
    Cell potionCell = new Cell(0, 1);
    Player player = new Player();

    MapObjectGroup<Potion> potionGroup = new MapObjectGroup<Potion>("invincible", Potion::new);
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
      // Thread.sleep(500);
    }
    LocalDateTime end = LocalDateTime.now();

    long period = Duration.between(start, end).getSeconds();
    System.out.println(period);
  }
}
