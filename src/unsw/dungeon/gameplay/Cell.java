package unsw.dungeon.gameplay;

import java.util.ArrayList;

public class Cell {
  private ArrayList<MapObject> mapObjects;
  private Cell[] adjacentCells;

  private int id;

  public Cell(int id) {
    this.id = id;
    mapObjects = new ArrayList<MapObject>();
    adjacentCells = new Cell[4];
    for (int i = 0; i < 4; i++) {
      adjacentCells[i] = null;
    }
  }

  public void setAdjacentCell(int direction, Cell cell) {
    adjacentCells[direction] = cell;
  }

  
  protected void setAdjacentCell(int direction, Cell cell)
  {
    adjacentCells[direction] = cell;
  }
  
  protected void addMapObject(MapObject obj) {
    mapObjects.add(obj);
  }
  
  /**
   * Can an entity walk into this cell?
   *
   * When calling this method, use a Direction.<attribute> value.
   *
   *
   */
  public boolean canWalkInto(int direction, MapObject object) {
    // iterate through objects in mapObject list to see if they are walkable
    for (MapObject obj : mapObjects) {
      if (!obj.canWalkInto(object, adjacentCells[direction])) {
        return false;
      }
    }
    
    return true;
  }
  
  /**
   *
   * When calling this method, use a Direction.<attribute> value.
   *
   *
   */
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

  // we don't support map larger than 65535x65535
  @Override
  public int hashCode() {
    return id;
  }
}
