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
public class MemoryUsageEventHandler extends AbstractHandler implements InitializingBean {

  private EPStatement memoryUsageEventStatement;

  @Autowired
  @Qualifier("memoryUsageEventSubscriber")
  private StatementSubscriber memoryUsageEventSubscriber;

  public void initService() {

    log.debug("Initializing memory usage event handler ..");
    Configuration config = new Configuration();
    config.configure();
    epService = EPServiceProviderManager.getDefaultProvider(config);

    createMemoryUsageMonitorExpression();
  }

  private void createMemoryUsageMonitorExpression() {
    log.debug("create memory usage Monitor");
    memoryUsageEventStatement = epService.getEPAdministrator().createEPL(memoryUsageEventSubscriber.getStatement(), "MemoryUsage statement");
    memoryUsageEventStatement.setSubscriber(memoryUsageEventSubscriber);
  }

  @Override
  public void afterPropertiesSet() {
    log.debug("Configuring..");
    initService();
  }
}
