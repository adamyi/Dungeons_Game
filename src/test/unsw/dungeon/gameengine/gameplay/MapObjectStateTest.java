package unsw.dungeon.gameengine.gameplay;

import org.junit.Test;

public class MapObjectStateTest {
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

    @Override
    public double viewOrder() {
      return 10;
    }
  }

  @Test
  public void addingRemovingStatesTest() {
    // add some states, remove some states.
    MapObjectState state1 = new MapObjectState("state1", Integer.MAX_VALUE);
    MapObjectState state2 = new MapObjectState("state2", Integer.MAX_VALUE);
    MapObjectState state3 = new MapObjectState("state3", 0);
    MapObjectState state4 = new MapObjectState("state4", 0);
    MapObjectState state5 = new MapObjectState("state5", 0);
    MapObjectState duplicateStateName = new MapObjectState("state1", 0);

    TestMapObject test = new TestMapObject(null);

    test.setState(state1);
    test.setState(state2);
    test.setState(state3);
    test.setState(state4);
    test.setState(state5);

    assert (test.getState("state1").isActive() == true);
    assert (test.getState("state2").isActive() == true);

    // lazy evaluation
    assert (test.getState("state1") != null);
    assert (test.getState("state2") != null);
    assert (test.getState("state3") == null);
    assert (test.getState("state4") == null);
  }
}
