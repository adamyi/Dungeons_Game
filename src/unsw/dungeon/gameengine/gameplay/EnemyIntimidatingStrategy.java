package unsw.dungeon.gameengine.gameplay;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

// different strategies to coordinate against player
//
// this strategy approaches player but tries to keep distance at 3
public class EnemyIntimidatingStrategy implements EnemyStrategy {
  private class BFSTuple {
    int distance;
    Cell cell;

    BFSTuple(int distance, Cell cell) {
      this.distance = distance;
      this.cell = cell;
    }

    protected int getDistance() {
      return distance;
    }

    protected Cell getCell() {
      return cell;
    }
  }

  private int weightedDistanceToPlayer(Enemy enemy, Cell cell) {
    if (cell == null || (!cell.canWalkInto(enemy))) return Integer.MAX_VALUE;
    HashSet<Cell> visited = new HashSet<>();
    Queue<BFSTuple> queue = new LinkedList<>();
    visited.add(cell);
    queue.add(new BFSTuple(0, cell));
    while (!queue.isEmpty()) {
      BFSTuple tup = queue.poll();
      Cell c = tup.getCell();
      Player p = (Player) c.getMapObjectOfType(Player.class);
      if (p != null) {
        return tup.getDistance();
      }
      for (int i = Direction.ITERATE_MIN; i <= Direction.ITERATE_MAX; i++) {
        Cell adjCell = c.getAdjacentCell(i);
        if (adjCell != null && (!visited.contains(adjCell)) && adjCell.canWalkInto(enemy)) {
          // this is actually an optimization for bfs
          // to do this here instead of in each iteration
          // cuz the first time a cell is added to queue
          // it should have lowest distance
          visited.add(adjCell);
          queue.add(new BFSTuple(tup.getDistance() + 1, adjCell));
        }
      }
    }
    return Integer.MAX_VALUE;
  }

  @Override
  public int getMove(Enemy enemy) {
    int mind =
        weightedDistanceToPlayer(enemy, enemy.getCell().getAdjacentCell(Direction.ITERATE_MIN));
    int maxd = mind;
    int mindir = Direction.ITERATE_MIN;
    int maxdir = Direction.ITERATE_MIN;
    for (int i = Direction.ITERATE_MIN + 1; i <= Direction.ITERATE_MAX; i++) {
      int d = weightedDistanceToPlayer(enemy, enemy.getCell().getAdjacentCell(i));
      if (d < mind) {
        mind = d;
        mindir = i;
      }
      if (d > maxd) {
        maxd = d;
        maxdir = i;
      }
    }
    if (mind > 3) return mindir;
    return maxdir;
  }
}
