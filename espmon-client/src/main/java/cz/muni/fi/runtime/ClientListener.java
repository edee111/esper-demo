package cz.muni.fi.runtime;

import cz.muni.fi.espmon.MBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.remote.JMXServiceURL;

/**
 * Listener for MBean proxies to handle notifications.
 *
 * @author Eduard Tomek
 * @since 27.11.14
 */
public class ClientListener implements NotificationListener {

  private Logger log = LoggerFactory.getLogger(ClientListener.class);
  private JMXServiceURL url;

  public ClientListener(JMXServiceURL url) {
    this.url = url;
  }

  @Override
  public void handleNotification(Notification notification, Object handback) {
    if (notification instanceof AttributeChangeNotification) {
      AttributeChangeNotification acn = (AttributeChangeNotification) notification;
      logNotification(acn);
    }
  }

  /**
   * Log MBean state
   *
   * @param acn nofitication
   */
  private void logNotification(AttributeChangeNotification acn) {
    Object newValue = acn.getNewValue();
    if (newValue instanceof MBean) {
      MBean mBean = (MBean) newValue;
      log.info(url + " " + mBean.getLogInfo());
    }
    else {
      log.error("New value " + newValue + " is not instance of MBean!");
    }
  }
}