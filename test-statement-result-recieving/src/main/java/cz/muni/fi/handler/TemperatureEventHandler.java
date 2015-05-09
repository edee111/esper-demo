package cz.muni.fi.handler;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.subscriber.CriticalEventSubscriber;
import cz.muni.fi.subscriber.MonitorEventSubscriber;
import cz.muni.fi.subscriber.StatementSubscriber;
import cz.muni.fi.subscriber.WarningEventSubscriber;
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
  //private EventRepresentation eventRepresentation;

  private TemperatureEventHandler(Configuration config) {
    log.debug("Initializing TemperatureEventHandler ..");
    epService = EPServiceProviderManager.getDefaultProvider(config);
    //eventRepresentation = representation;

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
    StatementSubscriber criticalEventSubscriber = new CriticalEventSubscriber();
    EPStatement criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventSubscriber.getStatement(), "TemperatureCriticalStatement");
    criticalEventStatement.setSubscriber(criticalEventSubscriber);
  }

  /**
   * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert subscriber.
   */
  private void createWarningTemperatureCheckExpression() {
    log.debug("creating Warning Temperature Check Expression");
    StatementSubscriber warningEventSubscriber = new WarningEventSubscriber();
    EPStatement warningEventStatement = epService.getEPAdministrator().createEPL(warningEventSubscriber.getStatement(), "TemperatureWarningStatement");
    warningEventStatement.setSubscriber(warningEventSubscriber);
  }

  /**
   * EPL to monitor the average temperature.
   */
  private void createTemperatureMonitorExpression() {
    log.debug("creating Timed Average Monitor");
    StatementSubscriber monitorEventSubscriber = new MonitorEventSubscriber();
    EPStatement monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventSubscriber.getStatement(), "TemperatureMonitorStatement");
    monitorEventStatement.setSubscriber(monitorEventSubscriber);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public static void handle(TemperatureEvent event) {
    getInstance().epService.getEPRuntime().sendEvent(event);
  }

  public static void handle(Map<String, Object> event) {
    getInstance().epService.getEPRuntime().sendEvent(event, TemperatureEvent.class.getSimpleName());
  }

  public static void handle(Object[] event) {
    getInstance().epService.getEPRuntime().sendEvent(event, TemperatureEvent.class.getSimpleName());
  }

  public static void handle(Node event) {
    getInstance().epService.getEPRuntime().sendEvent(event);
  }

  public static void init(Configuration config) {
    instance = new TemperatureEventHandler(config);
  }

  public synchronized static TemperatureEventHandler getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Instance was not initialized yet");
    }
    return instance;
  }

}
