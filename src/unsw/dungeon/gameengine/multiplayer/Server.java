package unsw.dungeon.gameengine.multiplayer;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import org.json.JSONObject;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.MapObjectGroup;
import unsw.dungeon.gameengine.Observer;
import unsw.dungeon.gameengine.Subject;
import unsw.dungeon.gameengine.gameplay.Cell;
import unsw.dungeon.gameengine.gameplay.MapObject;
import unsw.dungeon.gameengine.gameplay.Player;

public class Server implements Runnable, Observer {

  HashMap<String, Player> ip2players;
  HashMap<Integer, String> player2ips;
  Game game;
  DatagramSocket serverSocket;

  public Server(Game game, int serverPort) {
    ip2players = new HashMap<String, Player>();
    player2ips = new HashMap<Integer, String>();
    this.game = game;
    try {
      serverSocket = new DatagramSocket(serverPort);
      game.setSocket(serverSocket);
      System.out.printf("Listening on port %d\n", serverPort);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(Subject subject) {
    if (subject instanceof MapObject) {
      MapObject mapObject = (MapObject) subject;
      moveTo(mapObject, mapObject.getCell());
    }
  }

  public byte[] moveToData(MapObject mapObject, Cell cell) {
    JSONObject sendJSON = new JSONObject();
    sendJSON.put("i", mapObject.getId());
    sendJSON.put("t", mapObject.getTypeString());
    if (cell == null) {
      sendJSON.put("a", "d");
    } else {
      sendJSON.put("a", "m");
      if (!mapObject.getImage().equals(mapObject.initialImage()))
        sendJSON.put("g", mapObject.getImage());
      if (mapObject.getHue() != 0) sendJSON.put("h", mapObject.getHue());
      sendJSON.put("x", cell.getX());
      sendJSON.put("y", cell.getY());
    }
    byte[] sendData;
    try {
      sendData = sendJSON.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      e.printStackTrace();
      sendData = new byte[0];
    }
    return sendData;
  }

  private void sendDataToClients(byte[] sendData) {
    for (String astr : ip2players.keySet()) {
      sendDataToClient(astr, sendData);
    }
  }

  private void sendDataToClient(String astr, byte[] sendData) {
    try {
      int split = astr.lastIndexOf(":");
      String addrstr = astr.substring(0, split);
      String portstr = astr.substring(split + 1);
      InetAddress address = InetAddress.getByName(addrstr);
      int port = Integer.parseInt(portstr);
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
      serverSocket.send(sendPacket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void moveTo(MapObject obj, Cell cell) {
    if (cell == null) {
      sendDataToClients(moveToData(obj, cell));
      return;
    }
    Player p = cell.getPlayerOnly();
    if (p == null) {
      sendDataToClients(moveToData(obj, cell));
    } else {
      String pa = player2ips.get(p.getId());
      for (String astr : ip2players.keySet()) {
        if (astr.equals(pa)) sendDataToClient(astr, moveToData(obj, cell));
        else sendDataToClient(astr, moveToData(obj, null));
      }
    }
  }

  public void gameOver(boolean hasWon) {
    JSONObject sendJSON = new JSONObject();
    sendJSON.put("a", "o");
    sendJSON.put("w", hasWon);
    byte[] sendData;
    try {
      sendData = sendJSON.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      e.printStackTrace();
      sendData = new byte[0];
    }
    sendDataToClients(sendData);
  }

  @Override
  public void run() {
    try {

      // prepare buffers
      byte[] receiveData = new byte[1024];

      while (true) {
        if (!game.isRunning()) break;
        // receive UDP datagram
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);

        // get data
        String sentence = new String(receivePacket.getData());
        System.out.println("RECEIVED: " + sentence);

        // get info of the client with whom we are communicating
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        JSONObject data = new JSONObject(sentence);
        if (data.getString("a").equals("c")) {
          Player p = game.clonePlayer();
          String astr = String.format("%s:%d", IPAddress.toString().substring(1), port);
          ip2players.put(astr, p);
          player2ips.put(p.getId(), astr);
          JSONObject sendJSON = new JSONObject();
          sendJSON.put("a", "s");
          sendJSON.put("h", game.getHeight());
          sendJSON.put("w", game.getWidth());
          byte[] sendData;
          try {
            sendData = sendJSON.toString().getBytes("utf-8");
          } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            sendData = new byte[0];
          }
          DatagramPacket sendPacket =
              new DatagramPacket(sendData, sendData.length, IPAddress, port);
          serverSocket.send(sendPacket);
        } else if (data.getString("a").equals("s")) {
          HashMap<Class<? extends MapObject>, MapObjectGroup> groups = game.getMapObjectGroups();
          for (MapObjectGroup group : groups.values()) {
            for (int i = 0; i < group.getNumberOfMapObjects(); i++) {
              MapObject obj = group.getMapObject(i);
              Cell c = obj.getCell();
              if (c != null && c.getPlayerOnly() != null) c = null;
              byte[] sendData = moveToData(obj, c);
              DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
              serverSocket.send(sendPacket);
            }
          }
        } else if (data.getString("a").equals("a")) {
          String astr = String.format("%s:%d", IPAddress.toString().substring(1), port);
          ip2players.get(astr).makeMove(data.getInt("d"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    serverSocket.close();
  }
}
