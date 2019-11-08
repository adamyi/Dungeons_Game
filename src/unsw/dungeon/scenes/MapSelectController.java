package unsw.dungeon.scenes;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONObject;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameLoader;
import unsw.dungeon.utils.MapUtils;

public class MapSelectController {

  private HashMap<String, JSONObject> maps;

  @FXML private ListView<String> mapListView;
  @FXML private Button backButton;
  @FXML private Button playButton;

  public MapSelectController() {
    try {
      this.maps = MapUtils.getMaps(getClass());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void handlePlayButton() {
    try {
      Stage stage = (Stage) mapListView.getScene().getWindow();
      JSONObject map = maps.get(mapListView.getSelectionModel().getSelectedItem());
      GameLoader gameLoader = new GameLoader(map);
      Game game = gameLoader.load();
      GameController controller = new GameController(game, true);
      game.mpStart();

      FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
      loader.setController(controller);
      Parent root = loader.load();
      Scene scene =
          new Scene(
              root,
              game.getWidth() * controller.CELL_SIZE,
              game.getHeight() * controller.CELL_SIZE);
      scene.getRoot().requestFocus();

      stage.setScene(scene);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void handleBackButton() {
    try {
      Stage stage = (Stage) mapListView.getScene().getWindow();
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
  public void initialize() {
    mapListView.setCellFactory(
        param ->
            new ListCell<String>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) return;
                try {
                  setGraphic(new MapBoxController(item, maps.get(item)).getBox());
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });

    for (String mapname : maps.keySet()) {
      mapListView.getItems().add(mapname);
    }
  }
}
