package unsw.dungeon.gameengine.gameplay;

public abstract class Terrain extends MapObject {
  public Terrain() {
    super();
  }

  @Override
  public double viewOrder() {
    return 1000;
  }
}
