package unsw.dungeon.gameengine;

import java.net.DatagramSocket;
import java.util.HashMap;
import javafx.application.Platform;
import unsw.dungeon.gameengine.gameplay.*;
import unsw.dungeon.gameengine.multiplayer.Client;
import unsw.dungeon.gameengine.multiplayer.Server;
import unsw.dungeon.gameengine.objectives.ObjectiveNode;
import unsw.dungeon.scenes.GameController;

public class Game implements Observer {
  private Cell[][] grid;
  private HashMap<Class<? extends MapObject>, MapObjectGroup> mapObjectGroups;
  private HashMap<Integer, MapObject> mapObjects;
  private ObjectiveNode goal;
  private HashMap<String, Pairable> pairs;
  private int height;
  private int width;
  private int objCount;
  private GameController controller;
  private Server server;
  private Client client;
  private Thread mpThread;
  private Boolean running;
  private DatagramSocket socket;

  public Game(String server, int port) {
    this.height = 0;
    this.width = 0;
    init();
    this.client = new Client(this, server, port);
    mpThread = new Thread(client);
  }

  public Game(int height, int width) {
    init();
    setUpGrid(height, width);
    this.server = new Server(this, 6789);
    mpThread = new Thread(server);
  }

  public void mpStart() {
    mpThread.start();
  }

  private void init() {
    this.running = true;
    MapObjectHelper moh = new MapObjectHelper();
    this.mapObjectGroups = moh.newMapObjectGroups();
    this.pairs = new HashMap<>();
    HashMap<String, Class<? extends MapObject>> objToClass = moh.getObjectiveStringToClass();
    for (Class<? extends MapObject> cls : objToClass.values()) {
      mapObjectGroups.get(cls).attach(this);
    }
    mapObjectGroups.get(Player.class).attach(this);
    this.objCount = 0;
    this.mapObjects = new HashMap<>();
  }

  public void setUpGrid(int height, int width) {
    this.grid = new Cell[height + 2][width];
    this.height = height;
    this.width = width;
    for (int i = 0; i < height + 2; i++) {
      for (int j = 0; j < width; j++) {
        this.grid[i][j] = new Cell(j, i);
      }
    }

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (i > 0) this.grid[i][j].setAdjacentCell(Direction.UP, this.grid[i - 1][j]);
        if (i < height - 1) this.grid[i][j].setAdjacentCell(Direction.DOWN, this.grid[i + 1][j]);
        if (j > 0) this.grid[i][j].setAdjacentCell(Direction.LEFT, this.grid[i][j - 1]);
        if (j < width - 1) this.grid[i][j].setAdjacentCell(Direction.RIGHT, this.grid[i][j + 1]);
      }
    }
  }

  public void setGameController(GameController gameController) {
    this.controller = gameController;
    for (MapObjectGroup group : this.mapObjectGroups.values()) {
      for (int i = 0; i < group.getNumberOfMapObjects(); i++) {
        controller.setupMapObject(group.getMapObject(i));
      }
    }
  }

  public MapObject addMapObject(
      Class<? extends MapObject> type, int y, int x, HashMap<String, Object> properties) {
    MapObject obj = addMapObject(this.objCount, type, y, x, properties);
    this.objCount++;
    return obj;
  }

  public MapObject addMapObject(
      int id, Class<? extends MapObject> type, int y, int x, HashMap<String, Object> properties) {
    MapObject obj = this.mapObjectGroups.get(type).createNewMapObject(properties);
    if (Pairable.class.isInstance(obj)) {
      if (properties != null) {
        Pairable p = (Pairable) obj;
        String pk = String.format("%s_%d", p.getPairType(), (int) properties.get("id"));
        Pairable pp = pairs.get(pk);
        if (pp != null) {
          pp.setPair(p);
          p.setPair(pp);
        } else {
          pairs.put(pk, p);
        }
      }
    }
    obj.setId(id);
    this.mapObjects.put(id, obj);
    obj.moveTo(grid[y][x]);
    if (controller != null) {
      Platform.runLater(
          () -> {
            controller.setupMapObject(obj);
          });
    }
    if (server != null) {
      obj.attach(server);
    }
    if (type == Player.class) {
      ((Player) obj).setUpInventoryGrid(this.height, this.width);
      obj.attach(this);
    }
    return obj;
  }

  public MapObject getMapObjectOfId(int id) {
    return this.mapObjects.get(id);
  }

  public Player clonePlayer() {
    Player localplayer = (Player) mapObjectGroups.get(Player.class).getMapObject();
    Player newplayer =
        (Player)
            this.addMapObject(
                Player.class, localplayer.getCell().getY(), localplayer.getCell().getX(), null);
    return newplayer;
  }

  public Boolean isLocalPlayer(Player player) {
    if (player == null) return true;
    Player localplayer = (Player) mapObjectGroups.get(Player.class).getMapObject();
    return localplayer.getId() == player.getId();
  }

  public Cell getCell(int x, int y) {
    return grid[y][x];
  }

  public HashMap<Class<? extends MapObject>, MapObjectGroup> getMapObjectGroups() {
    return mapObjectGroups;
  }

  public void setObjective(ObjectiveNode goal) {
    this.goal = goal;
  }

  protected boolean hasWon() {
    return this.goal.hasWon(mapObjectGroups);
  }

  public int getHeight() {
    return this.height;
  }

  public int getDisplayHeight() {
    return this.height + 2;
  }

  public int getWidth() {
    return this.width;
  }

  public int getDisplayWidth() {
    return this.width;
  }

  public void makeMove(int action) {
    if (this.client == null) {
      Player player = (Player) mapObjectGroups.get(Player.class).getMapObject();
      player.makeMove(action);
    } else {
      this.client.makeMove(action);
    }
  }

  public void loop() {
    for (MapObjectGroup group : mapObjectGroups.values()) {
      group.act();
    }
  }

  public void printCLI() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        sb.append(grid[y][x].printCLI());
      }
      sb.append('\n');
    }
    System.out.println(sb.toString());
  }

  public void playCLIVersion() {
    try {
      HashMap<Character, Integer> actions = new HashMap<>();
      actions.put('h', Direction.LEFT);
      actions.put('l', Direction.RIGHT);
      actions.put('j', Direction.UP);
      actions.put('k', Direction.DOWN);
      while (true) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        this.printCLI();
        System.out.printf("\nWhat's your move? (h: left, l: right, j: up, k: down) ");
        System.out.flush();
        Character cmd = (char) System.in.read();
        Integer act = actions.get(cmd);
        if (act != null) {
          this.makeMove(act);
          this.loop();
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public void gameOver(boolean hasWon) {
    if (server != null) {
      server.gameOver(hasWon);
    }
    this.running = false;
    if (this.socket != null) this.socket.close();
    if (controller != null) {
      Platform.runLater(
          () -> {
            controller.gameOver(hasWon);
          });
    } else {
      throw new GameOverException(hasWon);
    }
  }

  public Boolean isRunning() {
    return this.running;
  }

  public void setSocket(DatagramSocket socket) {
    this.socket = socket;
  }

  @Override
  public void update(Subject subject) {
    if (subject instanceof MapObjectGroup) {
      MapObjectGroup mgr = (MapObjectGroup) subject;
      System.out.println(mgr.getName());
      if (mgr.getName().equals("player")) {
        gameOver(false);
      }
      if (this.hasWon()) {
        gameOver(true);
      }
    } else if (subject instanceof Player) {
      Player p = (Player) subject;
    }
  }
}
