package unsw.dungeon.scenes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.GameOverException;
import unsw.dungeon.gameengine.gameplay.Cell;
import unsw.dungeon.gameengine.gameplay.Direction;
import unsw.dungeon.gameengine.gameplay.MapObject;

/** A JavaFX controller for the dungeon. */
public class GameController {
  public static final double CELL_SIZE = 32;
  public static final double LOOP_INTERVAL_MS = 500;

  // @FXML private GridPane squares;
  @FXML private AnchorPane dungeonPane;

  private Game game;
  private HashMap<KeyCode, LocalDateTime> keysPressed;

  public GameController(Game game) {
    this.game = game;
    this.keysPressed = new HashMap<>();
  }

  @FXML
  public void initialize() {
    Image ground = new Image(getClass().getResourceAsStream("/images/dirt_0_new.png"));

    for (int x = 0; x < game.getWidth(); x++) {
      for (int y = 0; y < game.getHeight(); y++) {
        ImageView g = new ImageView(ground);
        g.setViewOrder(Double.POSITIVE_INFINITY);
        dungeonPane.getChildren().add(g);
        g.setTranslateX(x * CELL_SIZE);
        g.setTranslateY(y * CELL_SIZE);
      }
    }

    game.setGameController(this);

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(LOOP_INTERVAL_MS),
                event -> {
                  try {
                    game.loop();
                  } catch (GameOverException e) {
                    handleGameOverException(e);
                  }
                }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private void handleGameOverException(GameOverException e) {
    GameOverController controller;
    if (e.hasWon()) {
      System.out.println("won");
      controller = new GameOverController("You won!");

    } else {
      System.out.println("lost");
      controller = new GameOverController("You died!");
    }
    try {
      Stage stage = (Stage) dungeonPane.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOverView.fxml"));
      loader.setController(controller);
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (IOException ee) {
      throw new RuntimeException(ee);
    }
  }

  private Boolean hasPressed(KeyCode keyCode) {
    LocalDateTime lastPressed = keysPressed.get(keyCode);
    if (lastPressed != null
        && LocalDateTime.now().isBefore(lastPressed.plusNanos((long) LOOP_INTERVAL_MS * 1000000)))
      return true;
    return false;
  }

  @FXML
  public void handleKeyPress(KeyEvent event) throws IOException {
    if (hasPressed(event.getCode())) return;
    try {
      if (!(hasPressed(KeyCode.UP)
          || hasPressed(KeyCode.DOWN)
          || hasPressed(KeyCode.LEFT)
          || hasPressed(KeyCode.RIGHT))) {
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
      }
    } catch (GameOverException e) {
      handleGameOverException(e);
    }
    keysPressed.put(event.getCode(), LocalDateTime.now());
  }

  public void setupMapObject(MapObject mapObject) {
    Image img = new Image(getClass().getResourceAsStream("/images/" + mapObject.getImage()));
    ImageView node = new ImageView(img);
    node.setViewOrder(mapObject.viewOrder());
    // AnchorPane.setLeftAnchor(node, mapObject.getCell().getX() * CELL_SIZE);
    // AnchorPane.setTopAnchor(node, mapObject.getCell().getY() * CELL_SIZE);
    dungeonPane.getChildren().add(node);
    node.setTranslateX(mapObject.getCell().getX() * CELL_SIZE);
    node.setTranslateY(mapObject.getCell().getY() * CELL_SIZE);
    mapObject
        .cell()
        .addListener(
            new ChangeListener<Cell>() {
              @Override
              public void changed(
                  ObservableValue<? extends Cell> observable, Cell oldValue, Cell newValue) {
                if (newValue != null) {
                  node.setVisible(true);
                  if (oldValue == null) {
                    node.setTranslateX(newValue.getX() * CELL_SIZE);
                    node.setTranslateY(newValue.getY() * CELL_SIZE);
                  } else {
                    TranslateTransition tt =
                        new TranslateTransition(Duration.millis(LOOP_INTERVAL_MS), node);
                    tt.setFromX(oldValue.getX() * CELL_SIZE);
                    tt.setFromY(oldValue.getY() * CELL_SIZE);
                    tt.setToX(newValue.getX() * CELL_SIZE);
                    tt.setToY(newValue.getY() * CELL_SIZE);
                    tt.play();
                  }
                } else {
                  TranslateTransition tt =
                      new TranslateTransition(Duration.millis(LOOP_INTERVAL_MS * 0.5), node);
                  tt.setOnFinished(
                      event -> {
                        node.setVisible(false);
                      });
                  tt.play();
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
