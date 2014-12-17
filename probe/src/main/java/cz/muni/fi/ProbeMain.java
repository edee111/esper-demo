package cz.muni.fi;

import cz.muni.fi.util.SystemSnapshotTaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */
public class ProbeMain {

  /**
   * Logger
   */
  private static final Logger log = LoggerFactory.getLogger(ProbeMain.class);


  /**
   * Main method - start the Demo!
   */
  public static void main(String[] args) throws Exception {

    log.debug("Starting...");

    // Load spring config
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
    BeanFactory factory = (BeanFactory) appContext;

    // Start Demo
    SystemSnapshotTaker generator = (SystemSnapshotTaker) factory.getBean("eventGenerator");
    generator.startTakingSystemSnapshots();

  }

}
