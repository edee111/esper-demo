package cz.muni.fi.connector;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class CpuLoad implements CpuLoadMBean {

  private double load;
  private Date timestamp;

  public CpuLoad(double load, Date timestamp) {
    this.load = load;
    this.timestamp = timestamp;
  }

  @Override
  public double getLoad() {
    return load;
  }
  @Override
  public void setLoad(double load) {
    this.load = load;
  }
  @Override
  public Date getTimestamp() {
    return timestamp;
  }
  @Override
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "CpuLoadEvent{" +
            "load=" + load +
            ", timestamp=" + timestamp +
            '}';
  }

}
