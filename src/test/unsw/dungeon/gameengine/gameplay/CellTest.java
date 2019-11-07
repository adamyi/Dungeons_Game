package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;

public class CellTest {

  // need fake mapObject classwith stubs for playerInteraction() and canWalkInto()
  // can use a Boolean object for test
  class TestMapObject extends MapObject {
    public Boolean playerInteractionFlag;
    private Boolean canWalkIntoRet;

    protected TestMapObject(Boolean canWalkIntoRet) {
      super();
      this.canWalkIntoRet = canWalkIntoRet;
      playerInteractionFlag = false;
    }

    @Override
    protected boolean canWalkInto(MapObject object) {
      return canWalkIntoRet;
    }

    @Override
    protected void playerInteraction(Cell start, Player player) {
      playerInteractionFlag = true;
    }

    @Override
    protected StringBuilder printCLI() {
      return new StringBuilder("T");
    }

    @Override
    public String initialImage() {
      return "deep_elf_master_archer.png";
    }
  }

  protected class MapObjectChild1 extends MapObject {
    protected MapObjectChild1() {
      super();
    }

    @Override
    protected boolean canWalkInto(MapObject object) {
      return false;
    }

    @Override
    protected void playerInteraction(Cell start, Player player) {}

    @Override
    protected StringBuilder printCLI() {
      return new StringBuilder("T");
    }

    @Override
    public String initialImage() {
      return "deep_elf_master_archer.png";
    }
  }

  protected class MapObjectChild2 extends MapObject {
    protected MapObjectChild2() {
      super();
    }

    @Override
    protected boolean canWalkInto(MapObject object) {
      return false;
    }

    @Override
    protected void playerInteraction(Cell start, Player player) {}

    @Override
    protected StringBuilder printCLI() {
      return new StringBuilder("T");
    }

    @Override
    public String initialImage() {
      return "deep_elf_master_archer.png";
    }
  }

  // test adding and removing from mapObject ArrayList
  @Test
  public void arrayListMapObjectTest() {
    Cell testCell = new Cell(0, 0);

    TestMapObject first = new TestMapObject(null);
    TestMapObject second = new TestMapObject(null);
    TestMapObject third = new TestMapObject(null);
    TestMapObject fourth = new TestMapObject(null);
    TestMapObject fifth = new TestMapObject(null);
    TestMapObject sixth = new TestMapObject(null);

    testCell.addMapObject(first);
    testCell.addMapObject(second);

    assert (testCell.getMapObjectAtIndex(0) == first);
    assert (testCell.getMapObjectAtIndex(1) == second);
    assert (testCell.getNumberOfMapObjects() == 2);

    testCell.removeMapObject(first);

    assert (testCell.getMapObjectAtIndex(0) == second);
    assert (testCell.getNumberOfMapObjects() == 1);

    testCell.addMapObject(third);
    testCell.addMapObject(fourth);
    testCell.addMapObject(fifth);
    testCell.addMapObject(sixth);

    testCell.removeMapObject(fourth);
    testCell.removeMapObject(third);

    assert (testCell.getMapObjectAtIndex(0) == second);
    assert (testCell.getMapObjectAtIndex(1) == fifth);
    assert (testCell.getMapObjectAtIndex(2) == sixth);
    assert (testCell.getNumberOfMapObjects() == 3);
  }

  @Test
  public void getMapObjectOfTypeTest() {
    Cell testCell = new Cell(0, 0);

    MapObjectChild1 first = new MapObjectChild1();
    MapObjectChild1 second = new MapObjectChild1();
    MapObjectChild2 third = new MapObjectChild2();
    MapObjectChild1 fourth = new MapObjectChild1();
    MapObjectChild2 fifth = new MapObjectChild2();

    testCell.addMapObject(first);
    testCell.addMapObject(second);
    testCell.addMapObject(third);
    testCell.addMapObject(fourth);
    testCell.addMapObject(fifth);

    assert (testCell.getMapObjectOfType(TestMapObject.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild1.class) != null);
    assert (testCell.getMapObjectOfType(MapObjectChild2.class) != null);

    testCell.removeMapObject(third);
    testCell.removeMapObject(fifth);

    assert (testCell.getMapObjectOfType(TestMapObject.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild1.class) != null);
    assert (testCell.getMapObjectOfType(MapObjectChild2.class) == null);

    testCell.removeMapObject(first);
    testCell.removeMapObject(second);
    testCell.removeMapObject(fourth);

    assert (testCell.getMapObjectOfType(TestMapObject.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild1.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild2.class) == null);

    testCell.addMapObject(third);

    assert (testCell.getMapObjectOfType(TestMapObject.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild1.class) == null);
    assert (testCell.getMapObjectOfType(MapObjectChild2.class) != null);
  }

  @Test
  public void canWalkIntoTest() {
    Cell testCell = new Cell(0, 0);

    // empty test
    assert (testCell.canWalkInto(null) == true);

    // all can walk through
    TestMapObject first = new TestMapObject(true);
    TestMapObject second = new TestMapObject(true);
    TestMapObject third = new TestMapObject(true);
    TestMapObject fourth = new TestMapObject(true);

    testCell.addMapObject(first);
    testCell.addMapObject(second);
    testCell.addMapObject(third);
    testCell.addMapObject(fourth);

    assert (testCell.canWalkInto(null) == true);

    // one false
    TestMapObject fifth = new TestMapObject(false);

    testCell.addMapObject(fifth);

    assert (testCell.canWalkInto(null) == false);
  }

  @Test
  public void playerInteractionTest() {
    Cell testCell = new Cell(0, 0);

    TestMapObject first = new TestMapObject(true);
    TestMapObject second = new TestMapObject(true);
    TestMapObject third = new TestMapObject(true);

    testCell.addMapObject(first);
    testCell.addMapObject(second);
    testCell.addMapObject(third);

    testCell.playerInteraction(null, null);

    assert (first.playerInteractionFlag);
    assert (second.playerInteractionFlag);
    assert (third.playerInteractionFlag);
  }
}
