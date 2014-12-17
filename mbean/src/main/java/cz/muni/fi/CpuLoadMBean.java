package cz.muni.fi;

import javax.management.AttributeChangeNotification;
import javax.management.DynamicMBean;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public interface CpuLoadMBean extends MBeanInf {
  public Date getTimestamp();
  public void setTimestamp(Date t);
  public double getLoad();
  public void setLoad(double l);

  public MBeanNotificationInfo[] getNotificationInfo();
}
