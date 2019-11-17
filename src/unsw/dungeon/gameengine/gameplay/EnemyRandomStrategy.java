package unsw.dungeon.gameengine.gameplay;

import java.util.concurrent.ThreadLocalRandom;

// different strategies to coordinate against player
//
public class EnemyRandomStrategy implements EnemyStrategy {
  Enemy enemy;

  protected EnemyRandomStrategy(Enemy enemy) {
    this.enemy = enemy;
  }

  @Override
  public int getMove() {
    return ThreadLocalRandom.current().nextInt(Direction.ITERATE_MIN, Direction.ITERATE_MAX);
  }
}
