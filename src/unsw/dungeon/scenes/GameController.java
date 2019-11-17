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
import unsw.dungeon.gameengine.gameplay.Action;
import unsw.dungeon.gameengine.gameplay.Cell;
import unsw.dungeon.gameengine.gameplay.MapObject;

/** A JavaFX controller for the dungeon. */
public class GameController {
  public static final double CELL_SIZE = 32;
  public static final double LOOP_INTERVAL_MS = 300;

  // @FXML private GridPane squares;
  @FXML private AnchorPane dungeonPane;

  private Game game;
  private HashMap<KeyCode, LocalDateTime> keysPressed;
  private boolean authorative;

  public GameController(Game game, Boolean authorative) {
    this.game = game;
    this.keysPressed = new HashMap<>();
    this.authorative = authorative;
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

    if (authorative) {
      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  Duration.millis(LOOP_INTERVAL_MS),
                  event -> {
                    game.loop();
                  }));
      timeline.setCycleCount(Animation.INDEFINITE);
      timeline.play();
    }
  }

  public void gameOver(boolean hasWon) {
    GameOverController controller;
    if (hasWon) {
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
    if (!(hasPressed(KeyCode.UP)
        || hasPressed(KeyCode.DOWN)
        || hasPressed(KeyCode.LEFT)
        || hasPressed(KeyCode.RIGHT))) {
      switch (event.getCode()) {
        case UP:
          game.makeMove(Action.UP);
          break;
        case DOWN:
          game.makeMove(Action.DOWN);
          break;
        case LEFT:
          game.makeMove(Action.LEFT);
          break;
        case RIGHT:
          game.makeMove(Action.RIGHT);
          break;
        default:
          break;
      }
    }
    switch (event.getCode()) {
      case I:
        game.makeMove(Action.DRINK_INVINCIBILITY_POTION);
        break;
      default:
        break;
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
    Cell cell = mapObject.getCell();
    if (cell == null) {
      node.setVisible(false);
    } else {
      node.setTranslateX(cell.getX() * CELL_SIZE);
      node.setTranslateY(cell.getY() * CELL_SIZE);
    }
    mapObject
        .cell()
        .addListener(
            new ChangeListener<Cell>() {
              @Override
              public void changed(
                  ObservableValue<? extends Cell> observable, Cell oldValue, Cell newValue) {
                if (newValue != null) {
                  if (newValue.getPlayerOnly() != null
                      && !game.isLocalPlayer(newValue.getPlayerOnly())) {
                    node.setVisible(false);
                    return;
                  }
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
