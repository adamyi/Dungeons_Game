package unsw.dungeon.gameengine.gameplay;

import java.time.LocalDateTime;

public class MapObjectState {
  private String name;
  private Boolean forever;
  private LocalDateTime deadline;

  protected MapObjectState(String name, int seconds) {
    this.name = name;
    this.deadline = LocalDateTime.now();
    this.forever = false;
    this.extendDeadline(seconds);
  }

  protected boolean isActive() {
    return this.forever || this.deadline.isAfter(LocalDateTime.now());
  }

  protected void extendDeadline(int seconds) {
    if (seconds == Integer.MAX_VALUE) {
      this.forever = true;
      return;
    }
    if (!this.isActive()) {
      this.deadline = LocalDateTime.now();
    }
    this.deadline = this.deadline.plusSeconds(seconds);
  }

  protected String getName() {
    return this.name;
  }
}
