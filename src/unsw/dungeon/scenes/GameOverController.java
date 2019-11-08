package unsw.dungeon.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

  private String txt;

  @FXML private Label winLabel;

  @FXML private Button backButton;

  @FXML private Button exitButton;

  public GameOverController(String txt) {
    this.txt = txt;
  }

  public void initialize() {
    winLabel.setText(txt);
  }

  @FXML
  public void handleBackButton() {
    try {
      Stage stage = (Stage) winLabel.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelectView.fxml"));
      loader.setController(new ModeSelectController());
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
