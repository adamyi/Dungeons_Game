package unsw.dungeon.gameengine.gameplay;

public interface Pairable {
  public abstract void setPair(Pairable pair);

  public abstract Pairable getPair();

  public abstract String getPairType();
}
