package cz.muni.fi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
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

  /*
  public static void configureLogback() {
    // assume SLF4J is bound to logback in the current environment
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    try {
      JoranConfigurator configurator = new JoranConfigurator();
      configurator.setContext(context);
      // Call context.reset() to clear any previous configuration, e.g. default
      // configuration. For multi-step configuration, omit calling context.reset().
      context.reset();
      configurator.doConfigure();
    } catch (JoranException je) {
      // StatusPrinter will handle this
    }
    StatusPrinter.printInCaseOfErrorsOrWarnings(context);
  }
  */
}
