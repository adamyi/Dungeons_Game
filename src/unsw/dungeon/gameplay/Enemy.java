package unsw.dungeon.gameplay;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import unsw.dungeon.SharedConstants;

public class Enemy extends Entity implements AutonomousObject {
  public Enemy() {
    super();
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    if (player.getState(SharedConstants.PLAYER_INVINCIBLE_STATE) != null) {
      this.removeFromCell();
    } else {
      Sword sword = (Sword) player.getCollectibleOfTypeInInventory(Sword.class);
      if (sword != null) {
        sword.reduceDurability();
        this.removeFromCell();
      } else {
        player.die();
      }
    }
  }

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

  private int weightedDistanceToPlayer(Cell cell) {
    if (cell == null || (!cell.canWalkInto(this))) return Integer.MAX_VALUE;
    HashSet<Cell> visited = new HashSet<>();
    Queue<BFSTuple> queue = new LinkedList<>();
    visited.add(cell);
    queue.add(new BFSTuple(0, cell));
    while (!queue.isEmpty()) {
      BFSTuple tup = queue.poll();
      Cell c = tup.getCell();
      Player p = (Player) c.getMapObjectOfType(Player.class);
      if (p != null) {
        int weight = 1;
        // according to spec, only invincibility matters
        // enemy doesn't care about swords
        if (p.getState(SharedConstants.PLAYER_INVINCIBLE_STATE) != null) {
          weight = -1;
        }
        return weight * tup.getDistance();
      }
      for (int i = Direction.ITERATE_MIN; i <= Direction.ITERATE_MAX; i++) {
        Cell adjCell = c.getAdjacentCell(i);
        if (adjCell != null && (!visited.contains(adjCell))) {
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
  public void act() {
    if (this.getCell() == null) {
      // already died
      return;
    }
    int mind = weightedDistanceToPlayer(this.getCell().getAdjacentCell(Direction.ITERATE_MIN));
    int dir = Direction.ITERATE_MIN;
    boolean doMove = false;
    for (int i = Direction.ITERATE_MIN + 1; i <= Direction.ITERATE_MAX; i++) {
      int d = weightedDistanceToPlayer(this.getCell().getAdjacentCell(i));
      if (d != mind) {
        doMove = true;
        if (d < mind) {
          mind = d;
          dir = i;
        }
      }
    }
    if (doMove) {
      this.moveTo(dir);
    }
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("E");
  }
}
