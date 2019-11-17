package unsw.dungeon.gameengine.gameplay;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

// different strategies to coordinate against player
//
public class EnemyAStarStrategy implements EnemyStrategy {
  Enemy enemy;

  protected EnemyAStarStrategy(Enemy enemy) {
    this.enemy = enemy;
  }

  private class Node {
    int distance;
    Cell cell;
    int fScore;

    Node(int distance, Cell cell) {
      this.distance = distance;
      this.cell = cell;
      int fScore = Integer.MAX_VALUE;
    }

    protected int getDistance() {
      return distance;
    }

    protected Cell getCell() {
      return cell;
    }

    protected int getfScore() {
      return fScore;
    }

    protected void setfScore(int value) {
      this.fScore = value;
    }
  }

  private static Comparator<Node> nodeComparator =
      new Comparator<Node>() {
        @Override
        public int compare(Node a, Node b) {
          return a.getfScore() - b.getfScore();
        }
      };

  private int reconstructPath(HashMap<Cell, Cell> cameFrom, Cell current) {
    int distance = 0;
    // int direction =
    while (current != null) {
      current = cameFrom.get(current);
      distance++;
    }
    return distance;
  }

  // find path to player cell
  private int aStartForPlayer(Cell start, Cell player) {
    HashMap<Cell, Cell> cameFrom = new HashMap<>();
    PriorityQueue<Node> queue =
        new PriorityQueue<>(0, nodeComparator); // priority queue in order of fScore
    HashMap<Cell, Integer> gScore = new HashMap<>();
    HashMap<Cell, Integer> fScore = new HashMap<>();

    gScore.put(start, 0);
    fScore.put(start, h(start, player));

    cameFrom.put(start, null);

    Cell current = null;
    int min = 0;
    Cell neighbour;

    while (!queue.isEmpty()) {
      current = queue.poll().getCell();
      if (current.getMapObjectOfType(Player.class) != null) {
        return reconstructPath(cameFrom, current);
      }

      for (int i = Direction.ITERATE_MIN + 1; i <= Direction.ITERATE_MAX; i++) {
        // initial g score is infinity
        // cells that cannot be walked into are not considered
        neighbour = current.getAdjacentCell(i);
        if (gScore.get(neighbour) == null && neighbour.canWalkInto(this.enemy)) {
          gScore.put(neighbour, Integer.MAX_VALUE);
        }

        if (neighbour.canWalkInto(this.enemy)) {
          min = gScore.get(current) + d(neighbour);
          if (min < gScore.get(neighbour)) {
            cameFrom.put(neighbour, current);
            gScore.put(neighbour, min);
            fScore.put(neighbour, gScore.get(neighbour) + h(neighbour, player));
            if (!queue.contains(neighbour)) {
              Node neighbourNode = new Node(0, neighbour);
              neighbourNode.setfScore(fScore.get(neighbour));
              queue.add(neighbourNode);
            }
          }
        }
      }
    }

    return 0;
  }

  private int h(Cell start, Cell goal) {
    double x = Math.pow((double) (start.getX() - goal.getX()), 2.0);
    double y = Math.pow((double) (start.getY() - goal.getY()), 2.0);
    return (int) Math.floor(Math.sqrt(x + y));
  }

  private int d(Cell neighbour) {
    return neighbour.canWalkInto(this.enemy) ? 1 : 0;
  }

  // find nearest player using BFS
  private Cell playerBFS(Cell cell) {
    HashSet<Cell> visited = new HashSet<>();
    Queue<Node> queue = new LinkedList<>();
    visited.add(cell);
    queue.add(new Node(0, cell));
    while (!queue.isEmpty()) {
      Node tup = queue.poll();
      Cell c = tup.getCell();
      Player p = (Player) c.getMapObjectOfType(Player.class);
      if (p != null) { // found player
        return c;
      }
      for (int i = Direction.ITERATE_MIN; i <= Direction.ITERATE_MAX; i++) {
        Cell adjCell = c.getAdjacentCell(i);
        if (adjCell != null && (!visited.contains(adjCell) && adjCell.canWalkInto(this.enemy))) {
          // this is actually an optimization for bfs
          // to do this here instead of in each iteration
          // cuz the first time a cell is added to queue
          // it should have lowest distance
          visited.add(adjCell);
          queue.add(new Node(tup.getDistance() + 1, adjCell));
        }
      }
    }
    return null;
  }

  @Override
  public int getMove() {
    Cell player = playerBFS(this.enemy.getCell());
    if (player == null) {
      return Direction.UNKNOWN;
    }

    // return direction;
    return Direction.UNKNOWN;
  }
}
