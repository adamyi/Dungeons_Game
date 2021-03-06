package unsw.dungeon.gameengine.gameplay;

import java.util.HashMap;
import unsw.dungeon.gameengine.SharedConstants;

public class Enemy extends Entity implements AutonomousObject {
  private EnemyStrategy strategy;

  public Enemy() {
    super();
    this.strategy = new EnemySimpleStrategy();
  }

  public void setEnemyStrategy(EnemyStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public void initProperties(HashMap<String, Object> properties) {
    if (properties == null) return;
    if (properties.containsKey("strategy")) {
      String strategyKey = (String) properties.get("strategy");
      switch (strategyKey) {
        case "simple":
          this.strategy = new EnemySimpleStrategy();
          break;
        case "a_star":
          this.strategy = new EnemyAStarStrategy();
          break;
        case "still":
          this.strategy = new EnemyStillStrategy();
          break;
        case "random":
          this.strategy = new EnemyRandomStrategy();
          break;
        case "intimidating":
          this.strategy = new EnemyIntimidatingStrategy();
          break;
      }
    }
  }

  @Override
  protected boolean canWalkInto(MapObject object) {
    return Player.class.isInstance(object);
  }

  @Override
  protected void playerInteraction(Cell start, Player player) {
    if (player.getState(SharedConstants.PLAYER_INVINCIBLE_STATE) != null) {
      this.removeFromCell();
    } else {
      Sword sword = (Sword) player.getCollectibleOfTypeInInventory(Sword.class);
      if (sword != null) {
        sword.reduceDurability();
        this.removeFromCell();
      } else {
        player.die();
      }
    }
  }

  @Override
  public void act() {
    if (this.getCell() == null) {
      // already died
      return;
    }
    int dir = this.strategy.getMove(this);
    if (dir != Direction.UNKNOWN) this.moveTo(dir);
  }

  @Override
  protected StringBuilder printCLI() {
    return new StringBuilder("E");
  }

  @Override
  public String initialImage() {
    return "deep_elf_master_archer.png";
  }
}
