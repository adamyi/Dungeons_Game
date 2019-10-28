package unsw.dungeon;

import java.util.ArrayList;
import java.util.HashMap;

import unsw.dungeon.ObjectiveNode;
import unsw.dungeon.gameplay.Cell;
import unsw.dungeon.gameplay.MapObject;
import unsw.dungeon.gameplay.Player;

class Game {
  ArrayList<ArrayList<Cell>> grid;
  HashMap<Class, MapObjectGroup> mapObjectGroups;
  ObjectiveNode goal;
  
  public Game() {
    
  }
  
  protected int findDistance(Cell a, Cell b) {
    return 0;
  }
  
  protected boolean hasWon() {
    // RUN THROUGH OBJECTIVE NODE
    return false;
  }
  
}