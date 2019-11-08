package unsw.dungeon.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    System.out.println("clicked back");
  }

  @FXML
  public void handleExitButton() {
    System.exit(0);
  }
}
