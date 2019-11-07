package unsw.dungeon.utils;

import unsw.dungeon.gameengine.gameplay.Cell;
import unsw.dungeon.gameengine.gameplay.Direction;

public class DirectionUtils {
  public static int getOppositeDirection(int direction) {
    switch (direction) {
      case Direction.UP:
        return Direction.DOWN;

      case Direction.RIGHT:
        return Direction.LEFT;

      case Direction.DOWN:
        return Direction.UP;

      case Direction.LEFT:
        return Direction.RIGHT;

      default:
        return Direction.UNKNOWN;
    }
  }

  /**
   * Finds the direction of end relative to start
   *
   * @param start
   * @param end
   * @return Direction if cells are adjacent, Direction.UNKNOWN if not adjacent
   */
  public static int getDirectionBetweenAdjacentCells(Cell start, Cell end) {
    for (int direction = Direction.ITERATE_MIN; direction <= Direction.ITERATE_MAX; direction++) {
      if (start.getAdjacentCell(direction) == end) {
        return direction;
      }
    }
    return Direction.UNKNOWN;
  }
}
