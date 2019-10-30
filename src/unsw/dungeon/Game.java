package unsw.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import unsw.dungeon.gameplay.Cell;

class Game {
  ArrayList<ArrayList<Cell>> grid;
  HashMap<Class, MapObjectGroup> mapObjectGroups;
  ObjectiveNode goal;

  public Game() {}

  protected int findDistance(Cell a, Cell b) {
    return 0;
  }

  protected boolean hasWon() {
    // RUN THROUGH OBJECTIVE NODE
    return false;
  }
}
