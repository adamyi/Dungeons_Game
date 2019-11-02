package unsw.dungeon.gameplay;

public class Sword extends Collectible {
  private int durability;
  
  public Portion (int durability) {
    super();
    
    if (durability <= 0) {
      throw new IllegalArgumentException();
    }
    
    this.durability = durability;
  }
  
  protected int getDurability() {
    return durability;
  }
  
  protected void reduceDurability() {
    durability--;
    
    if (durability == 0) {
      this.owner.removeFromInventory(this);
    }
  }
}