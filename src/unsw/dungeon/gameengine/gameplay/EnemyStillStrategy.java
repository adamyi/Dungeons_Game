package unsw.dungeon.gameengine.gameplay;

// different strategies to coordinate against player
//
public class EnemyStillStrategy implements EnemyStrategy {
  Enemy enemy;

  protected EnemyStillStrategy(Enemy enemy) {
    this.enemy = enemy;
  }

  @Override
  public int getMove() {
    return Direction.UNKNOWN;
  }
}
