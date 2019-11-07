package unsw.dungeon;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameLoader;
import unsw.dungeon.scenes.GameController;

public class DungeonApplication extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("Dungeon");

    // DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("maze.json");
    // DungeonController controller = dungeonLoader.loadController();
    //
    GameLoader gameLoader = new GameLoader("boulders.json");
    Game game = gameLoader.load();
    GameController controller = new GameController(game);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/DungeonView.fxml"));
    loader.setController(controller);
    Parent root = loader.load();
    Scene scene =
        new Scene(
            root, game.getWidth() * controller.CELL_SIZE, game.getHeight() * controller.CELL_SIZE);
    root.requestFocus();
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) throws Exception {
    /* GameLoader gameLoader = new GameLoader("advanced.json");
    Game game = gameLoader.load();
    game.playCLIVersion(); */
    launch(args);
  }
}
