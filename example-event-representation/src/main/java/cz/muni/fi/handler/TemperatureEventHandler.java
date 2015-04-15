package cz.muni.fi.handler;

import com.espertech.esper.client.*;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.subscriber.CriticalEventSubscriber;
import cz.muni.fi.subscriber.MonitorEventSubscriber;
import cz.muni.fi.subscriber.WarningEventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.muni.fi.subscriber.StatementSubscriber;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureEventHandler {

  private static final Logger log = LoggerFactory.getLogger(TemperatureEventHandler.class);

  private static TemperatureEventHandler instance;

  /**
   * Esper service
   */
  private EPServiceProvider epService;
  private EPStatement criticalEventStatement;
  private EPStatement warningEventStatement;
  private EPStatement monitorEventStatement;

  private StatementSubscriber criticalEventSubscriber;
  private StatementSubscriber warningEventSubscriber;
  private StatementSubscriber monitorEventSubscriber;

  private TemperatureEventHandler(Configuration config) {
    log.debug("Initializing Service ..");
    epService = EPServiceProviderManager.getDefaultProvider(config);

    //todo proc nefunguje critical a warning dotazy na map a array eventech?
    //createCriticalTemperatureCheckExpression();
    createWarningTemperatureCheckExpression();
    createTemperatureMonitorExpression();
  }

  /**
   * EPL to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
   * than the first event. This is checking for a sudden, sustained escalating rise in the
   * temperature
   */
  private void createCriticalTemperatureCheckExpression() {
    log.debug("create Critical Temperature Check Expression");
    criticalEventSubscriber = new CriticalEventSubscriber();
    criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventSubscriber.getStatement(), "Temperature critical statement");
    criticalEventStatement.setSubscriber(criticalEventSubscriber);
  }

  /**
   * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert
   * listener.
   */
  private void createWarningTemperatureCheckExpression() {
    log.debug("create Warning Temperature Check Expression");
    warningEventSubscriber = new WarningEventSubscriber();
    warningEventStatement = epService.getEPAdministrator().createEPL(warningEventSubscriber.getStatement(), "Temperature warning statement");
    warningEventStatement.setSubscriber(warningEventSubscriber);
  }

  /**
   * EPL to monitor the average temperature every 10 seconds. Will call listener on every event.
   */
  private void createTemperatureMonitorExpression() {
    log.debug("create Timed Average Monitor");
    monitorEventSubscriber = new MonitorEventSubscriber();
    monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventSubscriber.getStatement(), "Temperature monitor statement");
    monitorEventStatement.setSubscriber(monitorEventSubscriber);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public static void handle(TemperatureEvent event) {
    getInstance().epService.getEPRuntime().sendEvent(event);
  }

  /**
   * Handle the incoming TemperatureEvent.
   */
  public static void handle(Map<String, Object> event) {
    getInstance().epService.getEPRuntime().sendEvent(event, TemperatureEvent.class.getSimpleName());
  }

  public static void handle(Object[] event) {
    getInstance().epService.getEPRuntime().sendEvent(event, TemperatureEvent.class.getSimpleName());
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
