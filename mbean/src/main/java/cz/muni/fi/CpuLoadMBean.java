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


  public void sayHello();

  public int add(int x, int y);

  public String getName();

  public int getCacheSize();
  public void setCacheSize(int size);
  public MBeanNotificationInfo[] getNotificationInfo();

  public String returnInfo();
}
