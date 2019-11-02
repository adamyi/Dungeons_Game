package unsw.dungeon;

import java.util.HashMap;
import unsw.dungeon.gameplay.*;

class Game {
  private Cell[][] grid;
  private HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups;
  private ObjectiveNode goal;
  private HashMap<String, Pairable> pairs;
  private int height;
  private int width;

  public Game(int height, int width) {
    this.grid = new Cell[height][width];
    this.height = height;
    this.width = width;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        this.grid[i][j] = new Cell(i * width + j);
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
    obj.moveTo(grid[y][x]);
  }

  protected boolean hasWon() {
    // RUN THROUGH OBJECTIVE NODE
    return false;
  }

  public void printCLI() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        sb.append(grid[y][x].printCLI());
      }
      sb.append('\n');
    }
    System.out.println(sb.toString());
  }

  public void playCLIVersion() {
    try {
      HashMap<Character, Integer> actions = new HashMap<>();
      actions.put('h', Direction.LEFT);
      actions.put('l', Direction.RIGHT);
      actions.put('j', Direction.UP);
      actions.put('k', Direction.DOWN);
      Player player = (Player) mapObjectGroups.get(Player.class).getMapObject(0);
      while (true) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        this.printCLI();
        System.out.printf("\nWhat's your move? (h: left, l: right, j: up, k: down) ");
        System.out.flush();
        Character cmd = (char) System.in.read();
        Integer act = actions.get(cmd);
        if (act != null) {
          player.moveTo(act);
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}
