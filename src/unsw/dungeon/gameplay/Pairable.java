package unsw.dungeon.gameplay;

public interface Pairable {
  public abstract void setPair(Pairable pair);

  public abstract Pairable getPair();

  public abstract String getPairType();
}
