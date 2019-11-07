package unsw.dungeon.gameengine.gameplay;

public abstract class Entity extends MapObject {
  public Entity() {
    super();
  }

  @Override
  public double viewOrder() {
    return 1;
  }
}
