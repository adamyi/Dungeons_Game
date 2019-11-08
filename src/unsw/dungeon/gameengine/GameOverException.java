package unsw.dungeon.gameengine;

public class GameOverException extends RuntimeException {
  private Boolean win;

  public GameOverException(Boolean win) {
    this.win = win;
  }

  public Boolean hasWon() {
    return win;
  }
}
