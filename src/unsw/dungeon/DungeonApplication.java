package unsw.dungeon;

// import javafx.application.Application;

public class DungeonApplication { // extends Application {

  public static void main(String[] args) throws Exception {
    GameLoader gameLoader = new GameLoader("advanced.json");
    Game game = gameLoader.load();
    game.playCLIVersion();
  }
}
