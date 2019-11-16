package unsw.dungeon;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameLoader;
import unsw.dungeon.scenes.ModeSelectController;

public class DungeonApplication extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("Dungeon");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/ModeSelectView.fxml"));
    loader.setController(new ModeSelectController());
    Parent root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setOnCloseRequest(e -> System.exit(0));
  }

  public static void main(String[] args) throws Exception {
    if (args.length > 0 && args[0].equals("--cli")) {
      GameLoader gameLoader = new GameLoader("advanced.json");
      Game game = gameLoader.load();
      game.playCLIVersion();
    } else {
      launch(args);
    }
  }
}
