package unsw.dungeon.gameplay;

import java.util.ArrayList;

public class Cell {
  private ArrayList<MapObject> mapObjects;
  private Cell[] adjacentCells;

  public Cell() {
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
  public boolean canWalkInto(int direction, Entity entity) {
    // iterate through objects in mapObject list to see if they are walkable
    for (MapObject obj : mapObjects) {
      if (!obj.canWalkInto(entity, adjacentCells[direction])) {
        return false;
      }
    }

    return true;
  }

  /** When calling this method, use a Direction.<attribute> value. */
  protected Cell getAdjacentCell(int direction) {
    return adjacentCells[direction];
  }

  protected MapObject getMapObjectAtIndex(int index) {
    return mapObjects.get(index);
  }

  protected MapObject getMapObjectOfType(Class type) {
    for (MapObject obj : mapObjects) {
      if (type.isInstance(obj)) {
        return obj;
      }
    }

    return null;
  }

  protected int getNumberOfMapObjects() {
    return mapObjects.size();
  }

  protected void playerInteraction(int direction, Player player) {
    for (MapObject obj : mapObjects) {
      obj.playerInteraction(adjacentCells[direction], player);
    }
  }

  protected void removeMapObject(MapObject obj) {
    for (int i = 0; i < mapObjects.size(); i++) {
      if (mapObjects.get(i) == obj) {
        mapObjects.remove(obj);
      }
    }
  }
}
