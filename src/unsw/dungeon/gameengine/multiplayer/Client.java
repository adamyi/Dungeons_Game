package unsw.dungeon.gameengine.multiplayer;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import org.json.JSONObject;
import unsw.dungeon.gameengine.Game;
import unsw.dungeon.gameengine.MapObjectHelper;
import unsw.dungeon.gameengine.gameplay.MapObject;

public class Client implements Runnable {

  Game game;
  DatagramSocket clientSocket;
  InetAddress serverAddress;
  int serverPort;
  private HashMap<String, Class<? extends MapObject>> typeToMapObjectClass;

  public Client(Game game, String server, int port) {
    try {
      MapObjectHelper moh = new MapObjectHelper();
      this.typeToMapObjectClass = moh.getMapObjectStringToClass();
      this.serverAddress = InetAddress.getByName(server);
      this.game = game;
      this.serverPort = port;
      clientSocket = new DatagramSocket();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void moveTo(int direction) {
    JSONObject data = new JSONObject();
    data.put("a", "a");
    data.put("d", direction);
    byte[] sendData;
    try {
      sendData = data.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    DatagramPacket sendPacket =
        new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
    try {
      clientSocket.send(sendPacket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void init(String action) {
    JSONObject data = new JSONObject();
    data.put("a", action);
    byte[] sendData;
    try {
      sendData = data.toString().getBytes("utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    DatagramPacket sendPacket =
        new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
    try {
      clientSocket.send(sendPacket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      init("c");

      // prepare buffers
      byte[] receiveData = new byte[1024];

      while (true) {
        // receive UDP datagram
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        // get data
        String sentence = new String(receivePacket.getData());
        System.out.println("RECEIVED: " + sentence);

        JSONObject data = new JSONObject(sentence);
        if (data.getString("a").equals("m")) {
          int id = data.getInt("i");
          MapObject obj = game.getMapObjectOfId(id);
          if (obj == null) {
            String type = data.getString("t");
            Class typeclass = typeToMapObjectClass.get(type);
            obj = game.addMapObject(id, typeclass, data.getInt("y"), data.getInt("x"), null);
          } else {
            obj.setCell(game.getCell(data.getInt("x"), data.getInt("y")));
          }
        } else if (data.getString("a").equals("d")) {
          int id = data.getInt("i");
          MapObject obj = game.getMapObjectOfId(id);
          if (obj != null) {
            obj.removeFromCell(false);
          }
        } else if (data.getString("a").equals("s")) {
          game.setUpGrid(data.getInt("h"), data.getInt("w"));
          init("s");
        } else if (data.getString("a").equals("o")) {
          game.gameOver(data.getBoolean("w"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
