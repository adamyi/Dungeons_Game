package unsw.dungeon.scenes;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameOverException;
import unsw.dungeon.gameengine.gameplay.Cell;
import unsw.dungeon.gameengine.gameplay.Direction;
import unsw.dungeon.gameengine.gameplay.MapObject;

/** A JavaFX controller for the dungeon. */
public class GameController {

  @FXML private GridPane squares;

  private Game game;

  public GameController(Game game) {
    this.game = game;
  }

  @FXML
  public void initialize() {
    Image ground = new Image(getClass().getResourceAsStream("/images/dirt_0_new.png"));

    for (int x = 0; x < game.getWidth(); x++) {
      for (int y = 0; y < game.getHeight(); y++) {
        squares.add(new ImageView(ground), x, y);
      }
    }

    game.setGameController(this);
  }

  @FXML
  public void handleKeyPress(KeyEvent event) throws IOException {
    try {
      switch (event.getCode()) {
        case UP:
          game.makeMove(Direction.UP);
          break;
        case DOWN:
          game.makeMove(Direction.DOWN);
          break;
        case LEFT:
          game.makeMove(Direction.LEFT);
          break;
        case RIGHT:
          game.makeMove(Direction.RIGHT);
          break;
        default:
          break;
      }
    } catch (GameOverException e) {
      GameOverController controller;
      if (e.hasWon()) {
        System.out.println("won");
        controller = new GameOverController("You won!");

      } else {
        System.out.println("lost");
        controller = new GameOverController("You died!");
      }
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOverView.fxml"));
      loader.setController(controller);
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    }
  }

  public void setupMapObject(MapObject mapObject) {
    Image img = new Image(getClass().getResourceAsStream("/images/" + mapObject.getImage()));
    ImageView node = new ImageView(img);
    GridPane.setColumnIndex(node, mapObject.getCell().getX());
    GridPane.setRowIndex(node, mapObject.getCell().getY());
    squares.getChildren().add(node);
    mapObject
        .cell()
        .addListener(
            new ChangeListener<Cell>() {
              @Override
              public void changed(
                  ObservableValue<? extends Cell> observable, Cell oldValue, Cell newValue) {
                if (newValue != null) {
                  node.setVisible(true);
                  GridPane.setColumnIndex(node, newValue.getX());
                  GridPane.setRowIndex(node, newValue.getY());
                } else {
                  node.setVisible(false);
                }
              }
            });
    mapObject
        .image()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Image img = new Image(getClass().getResourceAsStream("/images/" + newValue));
                node.setImage(img);
              }
            });
  }
}
