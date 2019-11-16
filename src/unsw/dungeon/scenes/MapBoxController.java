package unsw.dungeon.scenes;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;
import unsw.dungeon.utils.MapUtils;

public class MapBoxController {

  @FXML private GridPane box;

  @FXML private Label mapNameTextField;

  @FXML private Label mapSizeTextField;

  @FXML private ImageView mapThumbnailImageView;

  @FXML private Label winningConditionsTextField;

  public MapBoxController(String name, JSONObject map) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MapBox.fxml"));
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // set map info
    mapNameTextField.setText(name);
    mapSizeTextField.setText(String.format("%dx%d", map.getInt("width"), map.getInt("height")));

    // set map preview img
    Image thumbnail = null;
    try {
      thumbnail = new Image(getClass().getResourceAsStream("/thumbnails/question-mark.png"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    try {
      thumbnail = new Image(getClass().getResourceAsStream("/thumbnails/" + name + ".png"));
    } catch (Exception e) {
      System.err.println("Thumbnail image not found for " + name);
    }
    mapThumbnailImageView.setImage(thumbnail);
    mapThumbnailImageView.setPreserveRatio(true);
    mapThumbnailImageView.setFitHeight(200);
    mapThumbnailImageView.setFitWidth(200);

    winningConditionsTextField.setText(MapUtils.getWinningConditionsTextFromMap(map));
  }

  public GridPane getBox() {
    return box;
  }
}
