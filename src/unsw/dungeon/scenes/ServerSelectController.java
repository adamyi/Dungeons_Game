package unsw.dungeon.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import unsw.dungeon.gameengine.Game;

public class ServerSelectController {

  @FXML private Button backButton;

  @FXML private Button playButton;

  @FXML private TextField textServerAddress;

  @FXML
  public void handlePlayButton() {
    try {
      String astr = textServerAddress.getText();
      String addrstr = astr;
      int port = 6789;
      int split = astr.lastIndexOf(":");
      if (split > -1) {
        addrstr = astr.substring(0, split);
        String portstr = astr.substring(split + 1);
        port = Integer.parseInt(portstr);
      }

      Stage stage = (Stage) textServerAddress.getScene().getWindow();
      Game game = new Game(addrstr, port);
      GameController controller = new GameController(game, false);
      game.mpStart();

      Thread.sleep(1000);

      FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
      loader.setController(controller);
      Parent root = loader.load();
      Scene scene =
          new Scene(
              root,
              game.getDisplayWidth() * controller.CELL_SIZE,
              game.getDisplayHeight() * controller.CELL_SIZE);
      scene.getRoot().requestFocus();

      stage.setScene(scene);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void handleBackButton() {
    try {
      Stage stage = (Stage) textServerAddress.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelectView.fxml"));
      loader.setController(new ModeSelectController());
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
