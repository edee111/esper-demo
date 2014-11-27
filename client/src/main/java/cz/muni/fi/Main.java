package cz.muni.fi;

import cz.muni.fi.runtime.GlobalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    Core core = (Core) factory.getBean("core");
    core.run();
  }
}
