package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameOverException;
import unsw.dungeon.gameengine.MapObjectGroup;

public class PlayerEnemyCollisionTest {
  @Test
  public void playerDieTest() {
    Game game = new Game(1, 2);

    Player player = (Player) game.addMapObject(Player.class, 0, 0, null);
    Enemy enemy = (Enemy) game.addMapObject(Enemy.class, 0, 1, null);

    try {
      player.moveTo(enemy.getCell());
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
    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>("enemy", Enemy::new);
    Enemy enemy = enemyGroup.createNewMapObject(null);
    enemy.addToMapObjectGroup(enemyGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.RIGHT, playerCell);

    MapObjectGroup<Sword> swordGroup = new MapObjectGroup<Sword>("sword", Sword::new);
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
    MapObjectGroup<Enemy> enemyGroup = new MapObjectGroup<Enemy>("enemy", Enemy::new);
    Enemy enemy = enemyGroup.createNewMapObject(null);
    enemy.addToMapObjectGroup(enemyGroup);

    playerCell.setAdjacentCell(Direction.RIGHT, enemyCell);
    enemyCell.setAdjacentCell(Direction.LEFT, playerCell);

    MapObjectGroup<Potion> potionGroup = new MapObjectGroup<Potion>("invincible", Potion::new);
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
