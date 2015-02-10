package cz.muni.fi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import cz.muni.fi.config.EspMonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author Eduard Tomek
 * @since 6.11.14
 */
@Component
public class ClientMain {
  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);

  @Autowired
  private EspMonConfig config;
  @Autowired
  private Core core;
  private static ClientMain instance;

  public static void main(String[] args) {
    log.info("Starting EspMon...");
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
    BeanFactory factory = (BeanFactory) appContext;
    instance = (ClientMain) factory.getBean("clientMain");


    try {
      instance.configureLogback();
    } catch (EspMonException e) {
      log.error("Cannot configure logback", e);
      System.exit(1);
    }

    instance.core.run();
  }

  public void configureLogback() throws EspMonException {
    // assume SLF4J is bound to logback in the current environment
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(context);
    // Call context.reset() to clear any previous configuration, e.g. default
    // configuration. For multi-step configuration, omit calling context.reset().
    context.reset();
    try {
      configurator.doConfigure(config.getLogbackXmlPath());
    } catch (JoranException e) {
      throw new EspMonException("Log path is invalid", e);
    }

    StatusPrinter.printInCaseOfErrorsOrWarnings(context);
  }
}
