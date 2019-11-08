package unsw.dungeon.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ModeSelectController {

  @FXML private Label logoLabel;

  @FXML private Button newButton;
  @FXML private Button joinButton;
  @FXML private Button exitButton;

  @FXML
  public void handleNewButton() {
    try {
      Stage stage = (Stage) logoLabel.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("MapSelectView.fxml"));
      loader.setController(new MapSelectController());
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void handleJoinButton() {
    try {
      Stage stage = (Stage) logoLabel.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerSelectView.fxml"));
      loader.setController(new ServerSelectController());
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void handleExitButton() {
    System.exit(0);
  }
}
