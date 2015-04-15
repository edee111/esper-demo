package cz.muni.fi.runtime;

import cz.muni.fi.MBean;
import cz.muni.fi.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.remote.JMXServiceURL;
import java.text.SimpleDateFormat;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
public class ClientListener implements NotificationListener {

  private Logger log = LoggerFactory.getLogger(ClientListener.class);
  private JMXServiceURL url;

  public ClientListener(JMXServiceURL url) {
    this.url = url;
  }

  public void handleNotification(Notification notification,
                                 Object handback) {

    echo("\nReceived notification:");
    echo("\tClassName: " + notification.getClass().getName());
    echo("\tSource: " + notification.getSource());
    echo("\tType: " + notification.getType());
    echo("\tMessage: " + notification.getMessage());
    if (notification instanceof AttributeChangeNotification) {
      AttributeChangeNotification acn =
              (AttributeChangeNotification) notification;
      echo("\tAttributeName: " + acn.getAttributeName());
      echo("\tAttributeType: " + acn.getAttributeType());
      echo("\tNewValue: " + acn.getNewValue());
      echo("\tOldValue: " + acn.getOldValue());

      logNotification(acn);
    }
  }

  private static void echo(String msg) {
    //System.out.println(msg);
  }

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