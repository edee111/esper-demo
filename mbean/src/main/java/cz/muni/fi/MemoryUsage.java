package cz.muni.fi;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 13.11.14
 */
public class MemoryUsage extends MBean<MemoryUsageMBean> implements MemoryUsageMBean  {

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

  @Override
  public void update(MemoryUsageMBean mBean) {
    Notification n =
            new AttributeChangeNotification(this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "MemoryUsage changed",
                    "MemoryUsage",
                    "long",
                    this,
                    mBean);
    sendNotification(n);

    setTimestamp(mBean.getTimestamp());
    setUsage(mBean.getUsage());
  }
}
