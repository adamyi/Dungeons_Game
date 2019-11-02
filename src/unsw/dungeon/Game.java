package unsw.dungeon;

import java.util.HashMap;
import unsw.dungeon.gameplay.*;

class Game {
  private Cell[][] grid;
  private HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups;
  private ObjectiveNode goal;
  private HashMap<String, Pairable> pairs;

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
    this.pairs = new HashMap<>();
  }

  public void addMapObject(
      Class<? extends MapObject> type, int y, int x, HashMap<String, Object> properties) {
    MapObject obj = this.mapObjectGroups.get(type).createNewMapObject(properties);
    if (Pairable.class.isInstance(obj)) {
      Pairable p = (Pairable) obj;
      String pk = String.format("%s_%d", p.getPairType(), (int) properties.get("id"));
      Pairable pp = pairs.get(pk);
      if (pp != null) {
        pp.setPair(p);
        p.setPair(pp);
      } else {
        pairs.put(pk, p);
      }
    }
    grid[y][x].addMapObject(obj);
}
