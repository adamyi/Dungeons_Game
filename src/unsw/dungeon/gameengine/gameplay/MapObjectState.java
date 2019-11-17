package unsw.dungeon.gameengine.gameplay;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MapObjectState {
  private String name;
  private Boolean forever;
  private LocalDateTime deadline;

  public MapObjectState(String name, int seconds) {
    this.name = name;
    this.deadline = LocalDateTime.now();
    this.forever = false;
    this.extendDeadline(seconds);
  }

  public boolean isActive() {
    return this.forever || this.deadline.isAfter(LocalDateTime.now());
  }

  public void extendDeadline(int seconds) {
    if (seconds == Integer.MAX_VALUE) {
      this.forever = true;
      return;
    }
    if (!this.isActive()) {
      this.deadline = LocalDateTime.now();
    }
    this.deadline = this.deadline.plusSeconds(seconds);
  }

  public int getRemainingSeconds() {
    if (this.forever) return Integer.MAX_VALUE;
    return (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), this.deadline);
  }

  public String getName() {
    return this.name;
  }
}
