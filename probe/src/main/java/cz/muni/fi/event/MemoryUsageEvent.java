package cz.muni.fi.event;

import java.lang.management.MemoryUsage;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class MemoryUsageEvent extends AbstractEvent {
  private long usage;
  private Date timestamp;
  private long totalMemory;

  public MemoryUsageEvent(long totalMemory, long usage, Date timestamp) {
    this.usage = usage;
    this.timestamp = timestamp;
    this.totalMemory = totalMemory;
  }

  public long getTotalMemory() {
    return totalMemory;
  }

  public void setTotalMemory(long totalMemory) {
    this.totalMemory = totalMemory;
  }

  public long getUsage() {
    return usage;
  }

  public void setUsage(long usage) {
    this.usage = usage;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "MemoryUsageEvent{" +
            "usage=" + usage +
            ", timestamp=" + timestamp +
            '}';
  }
}
