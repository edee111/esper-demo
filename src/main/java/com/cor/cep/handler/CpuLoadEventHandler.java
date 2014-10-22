package com.cor.cep.handler;

import com.cor.cep.event.CpuLoadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.subscriber.StatementSubscriber;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * This class handles incoming Temperature Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
@Component
@Scope(value = "singleton")
public class CpuLoadEventHandler implements InitializingBean {

  /** Logger */
  private static Logger LOG = LoggerFactory.getLogger(TemperatureEventHandler.class);

  /** Esper service */
  private EPServiceProvider epService;
  private EPStatement cpuLoadEventStatement;

  @Autowired
  @Qualifier("cpuLoadEventSubscriber")
  private StatementSubscriber cpuLoadEventSubscriber;

  /**
   * Configure Esper Statement(s).
   */
  public void initService() {

    LOG.debug("Initializing Servcie ..");
    Configuration config = new Configuration();
    config.addEventTypeAutoName("com.cor.cep.event");
    epService = EPServiceProviderManager.getDefaultProvider(config);

    createCpuLoadMonitorExpression();
  }

  private void createCpuLoadMonitorExpression() {
    LOG.debug("create cpu load Monitor");
    cpuLoadEventStatement = epService.getEPAdministrator().createEPL(cpuLoadEventSubscriber.getStatement());
    cpuLoadEventStatement.setSubscriber(cpuLoadEventSubscriber);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public void handle(CpuLoadEvent event) {

    LOG.debug(event.toString());
    epService.getEPRuntime().sendEvent(event);

  }

  @Override
  public void afterPropertiesSet() {

    LOG.debug("Configuring..");
    initService();
  }
}
