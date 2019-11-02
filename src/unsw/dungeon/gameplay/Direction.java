package unsw.dungeon.gameplay;

public class Direction {
  public static final int UNKNOWN = -1;

  public static final int UP = 0;
  public static final int DOWN = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;

  public static final int ITERATE_MIN = 0;
  public static final int ITERATE_MAX = 3;
  public static final int SIZE = 4;

  public static int getOppositeDirection(int direction) {
    if (direction == UP) return DOWN;
    if (direction == RIGHT) return LEFT;
    if (direction == DOWN) return UP;
    if (direction == LEFT) return RIGHT;
    return UNKNOWN;
  }
}
