package unsw.dungeon.gameengine.gameplay;

// different strategies to coordinate against player
//
public class EnemyStillStrategy implements EnemyStrategy {
  @Override
  public int getMove(Enemy enemy) {
    return Direction.UNKNOWN;
  }
}
