package unsw.dungeon.gameplay;

import unsw.dungeon.MapObjectGroup;

public class Player extends Entity {
  public Player(MapObjectGroup group) {
    super(group);
  }

  @Override
  protected boolean canWalkInto(Entity entity, Cell next) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void playerInteraction(Cell next, Player player) {
    throw new UnsupportedOperationException();
  }
}
