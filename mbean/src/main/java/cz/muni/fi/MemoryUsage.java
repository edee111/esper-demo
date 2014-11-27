package cz.muni.fi;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 13.11.14
 */
public class MemoryUsage extends MBean implements MemoryUsageMBean  {

  private long usage;
  private Date timestamp;

  public MemoryUsage(long usage, Date timestamp) {
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
