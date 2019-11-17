package unsw.dungeon.gameengine.gameplay;

import java.util.ArrayList;

public class Player extends Entity {
  private ArrayList<Collectible> inventory;
  private Cell[] inventoryGrid;

  public Player() {
    super();
    this.inventory = new ArrayList<>();
    // this.setState(SharedConstants.PLAYER_INVINCIBLE_STATE, Integer.MAX_VALUE);
  }

  public void setUpInventoryGrid(int height, int width) {
    this.inventoryGrid = new Cell[width];
    for (int i = 0; i < width; i++) {
      this.inventoryGrid[i] = new Cell(i, height + 1, this);
    }

    for (int i = 0; i < width; i++) {
      if (i > 0) this.inventoryGrid[i].setAdjacentCell(Direction.LEFT, this.inventoryGrid[i - 1]);
      if (i < width - 1)
        this.inventoryGrid[i].setAdjacentCell(Direction.RIGHT, this.inventoryGrid[i + 1]);
    }
  }

  protected void die() {
    this.removeFromCell();
    // throw new GameOverException(false);
  }

  protected boolean hasObjectInInventory(Collectible object) {
    return inventory.contains(object);
  }

  protected void addToInventory(Collectible object) {
    inventory.add(object);
    if (inventoryGrid != null) object.moveTo(inventoryGrid[inventory.size() - 1]);
    else object.removeFromCell(false);
  }

  protected Collectible getCollectibleOfTypeInInventory(Class<? extends Collectible> type) {
    for (Collectible item : inventory) {
      if (type.isInstance(item)) return item;
    }
    return null;
  }

  protected void removeFromInventory(Collectible object) {
    int idx = inventory.indexOf(object);
    inventory.remove(object);
    if (inventoryGrid != null) {
      object.removeFromCell(false);
      for (int i = idx; i < inventory.size(); i++) {
        inventory.get(i).moveTo(inventoryGrid[i]);
      }
    }
  }

  public void makeMove(int action) {
    if (action >= Direction.ITERATE_MIN && action <= Direction.ITERATE_MAX) {
      this.moveTo(action);
    } else if (action == Action.DRINK_INVINCIBILITY_POTION) {
      Potion potion = (Potion) this.getCollectibleOfTypeInInventory(Potion.class);
      if (potion != null) potion.use();
    }
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    if (Player.class.isInstance(object)) return true;
    return object.canWalkInto(this);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {}

  @Override
  public void moveTo(Cell next) {
    Cell original = this.getCell();
    super.moveTo(next);
    this.getCell().playerInteraction(original, this);
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("P");
  }

  @Override
  public String initialImage() {
    return "human_new.png";
  }
}
