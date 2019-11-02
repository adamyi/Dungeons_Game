package unsw.dungeon.gameplay;

public class Direction {
  public static final int UNKNOWN = -1;

  public static final int UP = 0;
  public static final int DOWN = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;
  
  public static getOppositeDirection(int direction) {
    if (direction == TOP  ) return DOWN;
    if (direction == RIGHT) return LEFT;
    if (direction == DOWN ) return TOP;
    if (direction == LEFT ) return RIGHT;
    if (direction == UNKNOWN) return UNKNOWN;
  }
}
