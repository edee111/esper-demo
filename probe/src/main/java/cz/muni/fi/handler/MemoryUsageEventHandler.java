package cz.muni.fi.handler;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import cz.muni.fi.event.CpuLoadEvent;
import cz.muni.fi.event.MemoryUsageEvent;
import cz.muni.fi.subscriber.StatementSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Component
@Scope(value = "singleton")
public class MemoryUsageEventHandler implements InitializingBean {

  private static Logger log = LoggerFactory.getLogger(TemperatureEventHandler.class);

  /**
   * Esper service
   */
  private EPServiceProvider epService;
  private EPStatement memoryUsageEventStatement;

  @Autowired
  @Qualifier("memoryUsageEventSubscriber")
  private StatementSubscriber memoryUsageEventSubscriber;

  /**
   * Configure Esper Statement(s).
   */
  public void initService() {

    log.debug("Initializing memory usage event handler ..");
    Configuration config = new Configuration();
    config.addEventTypeAutoName("cz.muni.fi.event");
    epService = EPServiceProviderManager.getDefaultProvider(config);

    createMemoryUsageMonitorExpression();
  }

  private void createMemoryUsageMonitorExpression() {
    log.debug("create memory usage Monitor");
    memoryUsageEventStatement = epService.getEPAdministrator().createEPL(memoryUsageEventSubscriber.getStatement());
    memoryUsageEventStatement.setSubscriber(memoryUsageEventSubscriber);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public void handle(MemoryUsageEvent event) {

    //log.debug(event.toString());
    epService.getEPRuntime().sendEvent(event);

  }

  @Override
  public void afterPropertiesSet() {
    log.debug("Configuring..");
    initService();
  }
}
