package cz.muni.fi;

import cz.muni.fi.runtime.GlobalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 6.11.14
 */
public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    log.debug("Starting...");
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
    BeanFactory factory = (BeanFactory) appContext;
    GlobalClient globalClient = (GlobalClient) factory.getBean("globalClient");
    ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

    xrayExecutor.submit(new Runnable() {
      public void run() {
        log.debug(getStartingMessage());

        while (true) {
          //todo listen

          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            log.error("Thread Interrupted", e);
          }
        }

      }
    });
  }


  private static String getStartingMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n************************************************************");
    sb.append("\n* STARTING *");
    sb.append("\n************************************************************\n");
    return sb.toString();
  }
}
