package cz.muni.fi.handler;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import cz.muni.fi.ResultRecievingType;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.listener.BaseListener;
import cz.muni.fi.listener.CriticalEventListener;
import cz.muni.fi.listener.WarningEventListener;
import cz.muni.fi.subscriber.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureEventHandler {

  private static final Logger log = LoggerFactory.getLogger(TemperatureEventHandler.class);

  private static TemperatureEventHandler instance;

  private EPServiceProvider epService;
  private ResultRecievingType resultRecievingType;

  private TemperatureEventHandler(Configuration config, ResultRecievingType resultRecievingType) {
    log.debug("Initializing TemperatureEventHandler ..");
    epService = EPServiceProviderManager.getDefaultProvider(config);
    this.resultRecievingType = resultRecievingType;

    createCriticalTemperatureCheckExpression();
    createWarningTemperatureCheckExpression();
    createTemperatureMonitorExpression();

    log.debug("TemperatureEventHandler initialized");
  }

  /**
   * EPL to check for a sudden critical rise across 4 events. This is checking for a sudden, sustained escalating
   * rise in the temperature
   */
  private void createCriticalTemperatureCheckExpression() {
    log.debug("creating Critical Temperature Check Expression");
    EPStatement criticalEventStatement = null;
    switch (resultRecievingType) {
      case SUBSCRIBER:
        StatementSubscriber criticalEventSubscriber = new CriticalEventSubscriber();
        criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventSubscriber.getStatement(), "TemperatureCriticalStatement");
        criticalEventStatement.setSubscriber(criticalEventSubscriber);
      case LISTENER:
        BaseListener criticalEventListener = new CriticalEventListener();
        criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventListener.getStatement(), "TemperatureCriticalStatement");
        criticalEventStatement.addListener(criticalEventListener);
    }
  }

  /**
   * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert subscriber.
   */
  private void createWarningTemperatureCheckExpression() {
    log.debug("creating Warning Temperature Check Expression");
    EPStatement warningEventStatement = null;
    switch (resultRecievingType) {
      case SUBSCRIBER:
        StatementSubscriber warningEventSubscriber = new WarningEventSubscriber();
        warningEventStatement = epService.getEPAdministrator().createEPL(warningEventSubscriber.getStatement(), "TemperatureWarningStatement");
        warningEventStatement.setSubscriber(warningEventSubscriber);
      case LISTENER:
        BaseListener warningEventListener = new WarningEventListener();
        warningEventStatement = epService.getEPAdministrator().createEPL(warningEventListener.getStatement(), "TemperatureCriticalStatement");
        warningEventStatement.addListener(warningEventListener);
    }
  }

  /**
   * EPL to monitor the average temperature.
   */
  private void createTemperatureMonitorExpression() {
    log.debug("creating Timed Average Monitor");
    EPStatement monitorEventStatement = null;
    switch (resultRecievingType) {
      case SUBSCRIBER:
        StatementSubscriber monitorEventSubscriber = new WarningEventSubscriber();
        monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventSubscriber.getStatement(), "TemperatureWarningStatement");
        monitorEventStatement.setSubscriber(monitorEventSubscriber);
      case LISTENER:
        BaseListener monitorEventListener = new CriticalEventListener();
        monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventListener.getStatement(), "TemperatureCriticalStatement");
        monitorEventStatement.addListener(monitorEventListener);
    }
  }

  public static void handle(Object[] event) {
    getInstance().epService.getEPRuntime().sendEvent(event, TemperatureEvent.class.getSimpleName());
  }

  public static void init(Configuration config, ResultRecievingType type) {
    instance = new TemperatureEventHandler(config, type);
  }

  public synchronized static TemperatureEventHandler getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Instance was not initialized yet");
    }
    return instance;
  }

}
