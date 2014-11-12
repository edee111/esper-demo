package cz.muni.fi.event;

import java.lang.management.MemoryUsage;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class MemoryUsageEvent {
  private long usage;
  private Date timestamp;

  public MemoryUsageEvent(long usage, Date timestamp) {
    this.usage = usage;
    this.timestamp = timestamp;
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
