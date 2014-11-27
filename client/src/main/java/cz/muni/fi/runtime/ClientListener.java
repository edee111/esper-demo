package cz.muni.fi.runtime;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
public class ClientListener implements NotificationListener {
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
    }
  }

  private static void echo(String msg) {
    System.out.println(msg);
  }
}