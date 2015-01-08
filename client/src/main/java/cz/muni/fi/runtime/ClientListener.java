package cz.muni.fi.runtime;

import cz.muni.fi.MemoryUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
public class ClientListener implements NotificationListener {

  private Logger log = LoggerFactory.getLogger(ClientListener.class);

  public void handleNotification(Notification notification,
                                 Object handback) {

    //todo: tato metoda se spusti, kdyz se je poslana notifikace ... toto bude vsupni bod pro zmeny ... zde logovat
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

      //todo hack
      log.info(acn.getNewValue().toString());
    }
  }

  private static void echo(String msg) {
    //System.out.println(msg);
  }
}