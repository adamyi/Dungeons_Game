package unsw.dungeon.gameplay;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

import unsw.dungeon.MapObjectGroup;

public class CellTest {

  // need fake mapObject classwith stubs for playerInteraction() and canWalkInto()
  // can use a Boolean object for test
  class TestMapObject extends MapObject {
    public Boolean playerInteractionFlag;
    private Boolean canWalkIntoRet;
    
    protected TestMapObject(Boolean canWalkIntoRet) {
      super(null);
      this.canWalkIntoRet = canWalkIntoRet;
      playerInteractionFlag = false;
    }
    
    @Override
    protected boolean canWalkInto(Entity entity, Cell next) {
      return canWalkIntoRet;
    }
    
    @Override
    protected void playerInteraction(Cell next, Player player) {
      playerInteractionFlag = true;
    }
    
  }
  
  // test adding and removing from mapObject ArrayList
  @Test
  public void arrayListMapObjectTest() {
    Cell testCell = new Cell();
    
    TestMapObject first  = new TestMapObject(null);
    TestMapObject second = new TestMapObject(null);
    TestMapObject third  = new TestMapObject(null);
    TestMapObject fourth = new TestMapObject(null);
    TestMapObject fifth  = new TestMapObject(null);
    TestMapObject sixth  = new TestMapObject(null);
    
    testCell.addMapObject(first);
    testCell.addMapObject(second);
    
    assert(testCell.getMapObjectAtIndex(0) == first);
    assert(testCell.getMapObjectAtIndex(1) == second);
    assert(testCell.getNumberOfMapObjects() == 2);
    
    testCell.removeMapObject(first);
    
    assert(testCell.getMapObjectAtIndex(0) == second);
    assert(testCell.getNumberOfMapObjects() == 1);
    
    testCell.addMapObject(third);
    testCell.addMapObject(fourth);
    testCell.addMapObject(fifth);
    testCell.addMapObject(sixth);
    
    testCell.removeMapObject(fourth);
    testCell.removeMapObject(third);
    
    assert(testCell.getMapObjectAtIndex(0) == second);
    assert(testCell.getMapObjectAtIndex(1) == fifth);
    assert(testCell.getMapObjectAtIndex(2) == sixth);
    assert(testCell.getNumberOfMapObjects() == 3);
  }
  
  @Test
  public void canWalkIntoTest() {
    Cell testCell = new Cell();
    
    // empty test
    assert(testCell.canWalkInto(0, null) == true);
    
    // all can walk through
    TestMapObject first  = new TestMapObject(true);
    TestMapObject second = new TestMapObject(true);
    TestMapObject third  = new TestMapObject(true);
    TestMapObject fourth = new TestMapObject(true);
    
    testCell.addMapObject(first);
    testCell.addMapObject(second);
    testCell.addMapObject(third);
    testCell.addMapObject(fourth);
    
    assert(testCell.canWalkInto(0, null) == true);
    
    // one false
    TestMapObject fifth = new TestMapObject(false);
    
    testCell.addMapObject(fifth);
    
    assert(testCell.canWalkInto(0, null) == false);
  }
  
  @Test
  public void playerInteractionTest() {
    Cell testCell = new Cell();
    
    TestMapObject first  = new TestMapObject(true);
    TestMapObject second = new TestMapObject(true);
    TestMapObject third  = new TestMapObject(true);
    
    testCell.addMapObject(first);
    testCell.addMapObject(second);
    testCell.addMapObject(third);
    
    testCell.playerInteraction(0, null);
    
    assert(first.playerInteractionFlag);
    assert(second.playerInteractionFlag);
    assert(third.playerInteractionFlag);
  }
}

