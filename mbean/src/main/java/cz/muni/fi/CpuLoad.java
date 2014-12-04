package cz.muni.fi;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class CpuLoad extends MBean<CpuLoadMBean> implements CpuLoadMBean {

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
  public String returnInfo() {
    return "CpuLoad - Timestamp: " + getTimestamp() + ", Load: " + getLoad();
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

  @Override
  public void update(CpuLoadMBean mBean) {
    Notification n =
            new AttributeChangeNotification(this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "CpuLoad changed",
                    "CpuLoad",
                    "double",
                    this,
                    mBean);
    sendNotification(n);

    setLoad(mBean.getLoad());
    setTimestamp(mBean.getTimestamp());
  }
}
