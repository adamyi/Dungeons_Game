package unsw.dungeon.gameengine.gameplay;

import java.util.concurrent.ThreadLocalRandom;

// different strategies to coordinate against player
//
public class EnemyRandomStrategy implements EnemyStrategy {

  @Override
  public int getMove(Enemy enemy) {
    return ThreadLocalRandom.current().nextInt(Direction.ITERATE_MIN, Direction.ITERATE_MAX);
  }
}
