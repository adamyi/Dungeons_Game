package unsw.dungeon.gameplay;

import java.time.LocalDateTime;

public class MapObjectState {
  private String name;
  private LocalDateTime deadline;

  protected MapObjectState(String name) {
    this.name = name;
    this.deadline = LocalDateTime.now();
  }

  protected boolean isActive() {
    return this.deadline.isAfter(LocalDateTime.now());
  }

  protected void extendDeadline(int seconds) {
    if (!this.isActive()) {
      this.deadline = LocalDateTime.now();
    }
    this.deadline = this.deadline.plusSeconds(seconds);
  }

  protected String getName() {
    return this.name;
  }
}
