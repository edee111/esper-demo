package cz.muni.fi;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;

/**
 * @author Eduard Tomek
 * @since 13.11.14
 */
public abstract class MBean<T> extends NotificationBroadcasterSupport implements Serializable {

  @Override
  public MBeanNotificationInfo[] getNotificationInfo() {
    String[] types = new String[] {
            AttributeChangeNotification.ATTRIBUTE_CHANGE
    };
    String name = AttributeChangeNotification.class.getName();
    String description = "An attribute of this MBean has changed";
    MBeanNotificationInfo info =
            new MBeanNotificationInfo(types, name, description);
    return new MBeanNotificationInfo[] {info};
  }

  protected long sequenceNumber = 1;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public abstract void update(T mBean);
}