package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;
import unsw.dungeon.gameengine.GameOverException;
import unsw.dungeon.gameengine.MapObjectGroup;

public class PlayerEnemyCollisionTest {
  @Test
  public void playerDieTest() {
    Cell playerCell = new Cell(0, 0);
    Cell enemyCell = new Cell(0, 1);
    Player player = new Player();
    Enemy enemy = new Enemy();

    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.RIGHT, playerCell);

    try {
      player.moveTo(enemyCell);
      enemy.playerInteraction(playerCell, player);
      assert (false);
    } catch (GameOverException e) {
      assert (true);
    }
  }

  @Test
  public void playerSwordKillEnemyTest() {
    Cell playerCell = new Cell(0, 0);
    Cell enemyCell = new Cell(0, 1);
    Player player = new Player();
    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>(Enemy::new);
    Enemy enemy = enemyGroup.createNewMapObject(null);
    enemy.addToMapObjectGroup(enemyGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.RIGHT, playerCell);

    MapObjectGroup<Sword> swordGroup = new MapObjectGroup<Sword>(Sword::new);
    Sword sword = new Sword();
    sword.addToMapObjectGroup(swordGroup);

    sword.moveTo(playerCell);
    sword.playerInteraction(playerCell, player);
    assert (player.hasObjectInInventory(sword));

    player.moveTo(enemyCell);
    enemy.playerInteraction(playerCell, player);

    assert (enemyCell.getMapObjectOfType(Enemy.class) == null);
    assert (enemyGroup.getCounter() == 0);
  }

  @Test
  public void playerInvincibilityPotionKillEnemyTest() {
    Cell playerCell = new Cell(0, 0);
    Cell enemyCell = new Cell(0, 1);
    Player player = new Player();
    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>(Enemy::new);
    Enemy enemy = enemyGroup.createNewMapObject(null);
    enemy.addToMapObjectGroup(enemyGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.LEFT, playerCell);

    MapObjectGroup<Potion> potionGroup = new MapObjectGroup<Potion>(Potion::new);
    Potion potion = new Potion();
    potion.addToMapObjectGroup(potionGroup);

    potion.moveTo(playerCell);
    potion.playerInteraction(playerCell, player);
    assert (player.hasObjectInInventory(potion) == true);
    potion.use();
    assert (player.hasObjectInInventory(potion) == false);

    player.moveTo(enemyCell);
    enemy.playerInteraction(playerCell, player);

    assert (enemyCell.getMapObjectOfType(Enemy.class) == null);
    assert (enemyGroup.getCounter() == 0);
  }
}
