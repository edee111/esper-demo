package cz.muni.fi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Eduard Tomek
 * @since 6.11.14
 */
public class ClientMain {
  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);

  public static void main(String[] args) {

    log.debug("Starting...");
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
    BeanFactory factory = (BeanFactory) appContext;

    Core core = (Core) factory.getBean("core");
    core.run();
  }
}
