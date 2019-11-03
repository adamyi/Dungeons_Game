package unsw.dungeon.gameplay;

public class Sword extends Collectible {
  private int durability;

  public Sword() {
    this(5);
  }

  public Sword(int durability) {
    super();

    if (durability <= 0) {
      throw new IllegalArgumentException();
    }

    this.durability = durability;

    this.leaveAloneForeverWithCats();
  }

  protected int getDurability() {
    return durability;
  }

  protected void reduceDurability() {
    durability--;

    if (durability == 0) {
      this.getOwner().removeFromInventory(this);
    }
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("S");
  }
}
