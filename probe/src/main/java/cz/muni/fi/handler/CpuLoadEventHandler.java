package cz.muni.fi.handler;

import cz.muni.fi.event.CpuLoadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cz.muni.fi.subscriber.StatementSubscriber;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

@Component
@Scope(value = "singleton")
public class CpuLoadEventHandler extends AbstractHandler implements InitializingBean {

  private EPServiceProvider epService;
  private EPStatement cpuLoadEventStatement;

  @Autowired
  @Qualifier("cpuLoadEventSubscriber")
  private StatementSubscriber cpuLoadEventSubscriber;

  /**
   * Configure Esper Statement(s).
   */
  public void initService() {

    log.debug("Initializing Servcie ..");
    Configuration config = new Configuration();
    config.addEventTypeAutoName("cz.muni.fi.event");
    epService = EPServiceProviderManager.getDefaultProvider(config);

    createCpuLoadMonitorExpression();
  }

  private void createCpuLoadMonitorExpression() {
    log.debug("create cpu load Monitor");
    cpuLoadEventStatement = epService.getEPAdministrator().createEPL(cpuLoadEventSubscriber.getStatement());
    cpuLoadEventStatement.setSubscriber(cpuLoadEventSubscriber);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public void handle(CpuLoadEvent event) {

    //log.debug(event.toString());
    epService.getEPRuntime().sendEvent(event);

  }

  @Override
  public void afterPropertiesSet() {
    log.debug("Configuring..");
    initService();
  }
}
