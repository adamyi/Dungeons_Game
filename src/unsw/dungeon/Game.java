package unsw.dungeon;

import java.util.HashMap;
import org.json.JSONObject;
import unsw.dungeon.gameplay.*;

class Game {
  Cell[][] grid;
  HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups;
  ObjectiveNode goal;

  public Game(int height, int width) {
    this.grid = new Cell[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        this.grid[i][j] = new Cell();
      }
    }
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (i > 0) this.grid[i][j].setAdjacentCell(Direction.UP, this.grid[i - 1][j]);
        if (i < height - 1) this.grid[i][j].setAdjacentCell(Direction.DOWN, this.grid[i + 1][j]);
        if (j > 0) this.grid[i][j].setAdjacentCell(Direction.LEFT, this.grid[i][j - 1]);
        if (j < width - 1) this.grid[i][j].setAdjacentCell(Direction.RIGHT, this.grid[i][j + 1]);
      }
    }
    MapObjectHelper moh = new MapObjectHelper();
    this.mapObjectGroups = moh.newMapObjectGroups();
  }

  public void addMapObject(Class<? extends MapObject> type, int y, int x, JSONObject properties) {
    MapObject obj = this.mapObjectGroups.get(type).createNewMapObject();
    grid[y][x].addMapObject(obj);
  }

  protected boolean hasWon() {
    // RUN THROUGH OBJECTIVE NODE
    return false;
  }
}
