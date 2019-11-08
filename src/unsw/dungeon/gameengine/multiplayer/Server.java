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
  Game game;
  DatagramSocket serverSocket;

  public Server(Game game) {
    ip2players = new HashMap<String, Player>();
    this.game = game;
    int serverPort = 6789;
    try {
      serverSocket = new DatagramSocket(serverPort);
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
      sendJSON.put("x", cell.getX());
      sendJSON.put("y", cell.getY());
    }
    byte[] sendData;
    try {
      sendData = sendJSON.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return sendData;
  }

  private void sendDataToClients(byte[] sendData) {
    for (String astr : ip2players.keySet()) {
      try {
        int split = astr.lastIndexOf(":");
        String addrstr = astr.substring(0, split);
        String portstr = astr.substring(split + 1);
        InetAddress address = InetAddress.getByName(addrstr);
        int port = Integer.parseInt(portstr);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        serverSocket.send(sendPacket);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void moveTo(MapObject obj, Cell cell) {
    sendDataToClients(moveToData(obj, cell));
  }

  public void gameOver(boolean hasWon) {
    JSONObject sendJSON = new JSONObject();
    sendJSON.put("a", "o");
    sendJSON.put("w", hasWon);
    byte[] sendData;
    try {
      sendData = sendJSON.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    sendDataToClients(sendData);
  }

  @Override
  public void run() {
    try {

      // prepare buffers
      byte[] receiveData = new byte[1024];

      while (true) {
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
          JSONObject sendJSON = new JSONObject();
          sendJSON.put("a", "s");
          sendJSON.put("h", game.getHeight());
          sendJSON.put("w", game.getWidth());
          byte[] sendData;
          try {
            sendData = sendJSON.toString().getBytes("utf-8");
          } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
          }
          DatagramPacket sendPacket =
              new DatagramPacket(sendData, sendData.length, IPAddress, port);
          serverSocket.send(sendPacket);
        } else if (data.getString("a").equals("s")) {
          HashMap<Class<? extends MapObject>, MapObjectGroup> groups = game.getMapObjectGroups();
          for (MapObjectGroup group : groups.values()) {
            for (int i = 0; i < group.getNumberOfMapObjects(); i++) {
              byte[] sendData = moveToData(group.getMapObject(i), group.getMapObject(i).getCell());
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
  }
}
