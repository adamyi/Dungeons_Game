package unsw.dungeon.gameengine.gameplay;

import java.util.ArrayList;

public class Cell {
  public static final int CLI_MODE_PRINT_LENGTH = 4;
  private ArrayList<MapObject> mapObjects;
  private Cell[] adjacentCells;

  private int x;
  private int y;

  public Cell(int x, int y) {
    this.x = x;
    this.y = y;
    mapObjects = new ArrayList<MapObject>();
    adjacentCells = new Cell[4];
    for (int i = 0; i < 4; i++) {
      adjacentCells[i] = null;
    }
  }

  public void setAdjacentCell(int direction, Cell cell) {
    adjacentCells[direction] = cell;
  }

  public void addMapObject(MapObject obj) {
    mapObjects.add(obj);
  }

  /**
   * Can an entity walk into this cell?
   *
   * <p>When calling this method, use a Direction.<attribute> value.
   */
  public boolean canWalkInto(MapObject object) {
    // iterate through objects in mapObject list to see if they are walkable
    for (MapObject obj : mapObjects) {
      if (!obj.canWalkInto(object)) {
        return false;
      }
    }

    return true;
  }

  /** When calling this method, use a Direction.<attribute> value. */
  public Cell getAdjacentCell(int direction) {
    return adjacentCells[direction];
  }

  public MapObject getMapObjectAtIndex(int index) {
    return mapObjects.get(index);
  }

  protected MapObject getMapObjectOfType(Class<? extends MapObject> type) {
    for (MapObject obj : mapObjects) {
      if (type.isInstance(obj)) {
        return obj;
      }
    }

    return null;
  }

  public int getNumberOfMapObjects() {
    return mapObjects.size();
  }

  protected void playerInteraction(Cell start, Player player) {
    for (MapObject obj : mapObjects) {
      obj.playerInteraction(start, player);
    }
  }

  protected void removeMapObject(MapObject obj) {
    for (int i = 0; i < mapObjects.size(); i++) {
      if (mapObjects.get(i) == obj) {
        mapObjects.remove(obj);
      }
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // we don't support map larger than 65535x65535
  @Override
  public int hashCode() {
    return (x << 16) | y;
  }

  public StringBuilder printCLI() {
    StringBuilder sb = new StringBuilder("#");
    for (MapObject obj : mapObjects) {
      sb.append(obj.printCLI());
    }
    for (int i = sb.length(); i < CLI_MODE_PRINT_LENGTH; i++) {
      sb.append(" ");
    }
    return sb;
  }
}
