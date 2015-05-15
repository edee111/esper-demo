package cz.muni.fi.espmon.teststatementresultrecieving.handler;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import cz.muni.fi.espmon.teststatementresultrecieving.ResultRecievingType;
import cz.muni.fi.espmon.teststatementresultrecieving.event.TemperatureEvent;
import cz.muni.fi.espmon.teststatementresultrecieving.listener.*;
import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    String statementName = "TemperatureCriticalStatement";

    if (ResultRecievingType.SUBSCRIBER.equals(resultRecievingType)) {
      StatementSubscriber subscriber = new CriticalEventSubscriber();
      criticalEventStatement = epService.getEPAdministrator().createEPL(subscriber.getStatement(), statementName);
      criticalEventStatement.setSubscriber(subscriber);
    }
    else if (ResultRecievingType.LISTENER.equals(resultRecievingType)) {
      BaseListener listener = new CriticalEventListener();
      criticalEventStatement = epService.getEPAdministrator().createEPL(listener.getStatement(), statementName);
      criticalEventStatement.addListener(listener);
    }
  }

  /**
   * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert subscriber.
   */
  private void createWarningTemperatureCheckExpression() {
    log.debug("creating Warning Temperature Check Expression");
    EPStatement warningEventStatement = null;
    String statementName = "TemperatureWarningStatement";
    if (ResultRecievingType.SUBSCRIBER.equals(resultRecievingType)) {
      StatementSubscriber subscriber = new WarningEventSubscriber();
      warningEventStatement = epService.getEPAdministrator().createEPL(subscriber.getStatement(), statementName);
      warningEventStatement.setSubscriber(subscriber);
    }
    else if (ResultRecievingType.LISTENER.equals(resultRecievingType)) {
      BaseListener listener = new WarningEventListener();
      warningEventStatement = epService.getEPAdministrator().createEPL(listener.getStatement(), statementName);
      warningEventStatement.addListener(listener);
    }
  }

  /**
   * EPL to monitor the average temperature.
   */
  private void createTemperatureMonitorExpression() {
    log.debug("creating Timed Average Monitor");
    EPStatement monitorEventStatement = null;
    String statementName = "TemperatureMonitorStatement";
    if (ResultRecievingType.SUBSCRIBER.equals(resultRecievingType)) {
      StatementSubscriber subscriber = new MonitorEventSubscriber();
      monitorEventStatement = epService.getEPAdministrator().createEPL(subscriber.getStatement(), statementName);
      monitorEventStatement.setSubscriber(subscriber);
    }
    else if (ResultRecievingType.LISTENER.equals(resultRecievingType)) {
      MonitorEventListener listener = new MonitorEventListener();
      monitorEventStatement = epService.getEPAdministrator().createEPL(listener.getStatement(), statementName);
      monitorEventStatement.addListener(listener);
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
