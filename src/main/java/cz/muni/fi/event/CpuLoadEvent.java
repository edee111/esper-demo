package cz.muni.fi.event;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since ${date}
 */
public class CpuLoadEvent {

  private double load;
  private Date timestamp;

  public CpuLoadEvent(double load, Date timestamp) {
    this.load = load;
    this.timestamp = timestamp;
  }

  public double getLoad() {
    return load;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "CpuLoadEvent{" +
            "load=" + load +
            ", timestamp=" + timestamp +
            '}';
  }
}
