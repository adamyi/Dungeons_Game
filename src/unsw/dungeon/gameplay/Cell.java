package unsw.dungeon.gameplay;

import java.util.ArrayList;

import unsw.dungeon.gameplay.Direction;
import unsw.dungeon.gameplay.MapObject;
import unsw.dungeon.gameplay.Player;

public class Cell {
  ArrayList<MapObject> mapObjects;
  Cell[] adjacentCells;
  
  public Cell(Cell adjTop, Cell adjRight, Cell adjDown, Cell adjLeft) {
    this.mapObjects = new ArrayList<MapObject>();
    this.adjacentCells = new Cell[4];
    this.adjacentCells[0] = adjTop;
    this.adjacentCells[1] = adjRight;
    this.adjacentCells[2] = adjDown;
    this.adjacentCells[3] = adjLeft;
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
  public boolean canWalkInto(int direction, Entity entity) {
    // iterate through objects in mapObject list to see if they are walkable
    for (MapObject obj : mapObjects) {
      if (!obj.canWalkInto(entity, adjacentCells[direction])) {
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
