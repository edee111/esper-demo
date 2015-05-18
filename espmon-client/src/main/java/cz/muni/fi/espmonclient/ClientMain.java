package cz.muni.fi.espmonclient;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import cz.muni.fi.espmonclient.config.EspMonClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Main class for espmon-client module
 *
 * @author Eduard Tomek
 * @since 6.11.14
 */
@Component
public class ClientMain {
  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);

  @Autowired
  private EspMonClientConfig config;
  @Autowired
  private JMXClientManager JMXClientManager;
  private static ClientMain instance;

  public static void main(String[] args) {
    log.info("Starting EspMon...");
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
    BeanFactory factory = appContext;
    instance = (ClientMain) factory.getBean("clientMain");


    try {
      instance.configureLogback();
    } catch (EspmonClientException e) {
      log.error("Cannot configure logback", e);
      System.exit(1);
    }

    instance.JMXClientManager.run();
  }

  /**
   * Configure logback by values from EspmonClientConfig class
   *
   * @throws EspmonClientException if logback file not found
   */
  public void configureLogback() throws EspmonClientException {
    // assume SLF4J is bound to logback in the current environment
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(context);
    // Call context.reset() to clear any previous configuration, e.g. default
    // configuration. For multi-step configuration, omit calling context.reset().
    context.reset();
    context.putProperty("espmon-logdir", config.getLogsDirPath());
    try {
      configurator.doConfigure(config.getLogbackXmlPath());
    } catch (JoranException e) {
      throw new EspmonClientException("Log path is invalid", e);
    }

    StatusPrinter.printInCaseOfErrorsOrWarnings(context);
  }
}
