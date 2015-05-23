package cz.muni.fi.espmon.teststatementresultrecieving;

import com.espertech.esper.client.Configuration;
import cz.muni.fi.espmon.teststatementresultrecieving.monitor.TemperatureMonitorSingleRun;
import cz.muni.fi.espmonjmx.EsperMetricsMonitor;
import cz.muni.fi.espmonjmx.EspmonJMXException;
import cz.muni.fi.espmon.teststatementresultrecieving.event.TemperatureEvent;
import cz.muni.fi.espmon.teststatementresultrecieving.handler.TemperatureEventHandler;
import cz.muni.fi.espmon.teststatementresultrecieving.monitor.TemperatureMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of module
 *
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class MainResultReceiving {
  private static final int SERVER_COUNT = 4000;
  private static final int SERVER_PER_THREAD_COUNT = 1000;

  private static final Logger log = LoggerFactory.getLogger(MainResultReceiving.class);

  private static final int ENGINE_INTERVAL = 5000;
  private static final int STATEMENT_INTERVAL = 5000;


  /**
   * Main method of module
   *
   * @param args arguments, which are durationInSecords recievingType(SUBSCRIBER|LISTENER) portNumber
   * @throws EspmonJMXException
   */
  public static void main(String[] args) throws EspmonJMXException {
    int duration;
    ResultReceivingType recievingType;
    int port;

    if (args.length == 3) {
        duration = Integer.valueOf(args[0]);
        recievingType = ResultReceivingType.valueOfStr(args[1]);
        port = Integer.valueOf(args[2]);
    }
    else {
      log.error("Incorrect number of arguments. Arguments are: " +
              "durationInSeconds recievingType(SUBSCRIBER|LISTENER) portNumber");
      return;
    }

    runTest(duration, recievingType, port);
  }

  private static void runTest(int durationInSeconds, ResultReceivingType resultReceivingType, int port) throws EspmonJMXException {
    Configuration config = new Configuration();
    registerEventType(config);

    /*
     * Not all statements may report metrics: The engine performs certain jmx optimizations sharing resources
     * between similar statements, thereby not reporting on certain statements unless resource sharing
     * is disabled through configuration.
     * Esper refference 16.4.12.1.
     * We want real statement metrics results for purpose of this test. Optimization disabled.
     */
    config.getEngineDefaults().getViewResources().setShareViews(false);

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, ENGINE_INTERVAL, STATEMENT_INTERVAL, port);

    TemperatureEventHandler.init(config, resultReceivingType);
    runExecution(durationInSeconds);
  }

  private static void runExecution(int durationInSeconds) throws EspmonJMXException {
    List<TemperatureMonitor> monitors = new ArrayList<>();
    for (int i = 1; i <= SERVER_COUNT; i++) {
      monitors.add(new TemperatureMonitor(String.valueOf(i)));
    }

    long runEndTime = durationInSeconds * 1000 + System.currentTimeMillis();
    int defaultSleepTime = 1000 / TemperatureMonitor.EVENT_PER_SEC_COUNT;
    Collection<TemperatureMonitorSingleRun> runs = prepareRuns();
    ExecutorService exeSvc = Executors.newFixedThreadPool(SERVER_COUNT / SERVER_PER_THREAD_COUNT);
    while (true) {
      long start = System.currentTimeMillis();

      try {
        exeSvc.invokeAll(runs);
      } catch (InterruptedException e) {
        log.error("Exception while invoking monitors", e);
        throw new EspmonJMXException(e);
      }

      if (System.currentTimeMillis() > runEndTime) {
        break;
      }

      waitUntilNextIteration(defaultSleepTime, start);
    }

    stop(exeSvc);
  }

  private static Collection<TemperatureMonitorSingleRun> prepareRuns() {
    Collection<TemperatureMonitorSingleRun> runs = new ArrayList<>();
    int i;
    for (i = 1; i < SERVER_COUNT; i += SERVER_PER_THREAD_COUNT) {
      List<TemperatureMonitor> monitorsForRun = createMonitors(i, i + SERVER_PER_THREAD_COUNT);
      runs.add(new TemperatureMonitorSingleRun(monitorsForRun));
    }

    return runs;
  }

  private static List<TemperatureMonitor> createMonitors(int from, int to) {
    List<TemperatureMonitor> monitors = new ArrayList<>();
    for (int i = from; i < to; i++) {
      monitors.add(new TemperatureMonitor(String.valueOf(i)));
    }

    return monitors;
  }

  private static void waitUntilNextIteration(int defaultSleepTime, long start) throws EspmonJMXException {
    long executionDuration = System.currentTimeMillis() - start;
    long sleepTime = defaultSleepTime - executionDuration;
    if (sleepTime < 0) {
      log.warn("Execution is running late, cannot process so much data in given time period.");
    }
    else {
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        log.error("Interrupted exception while running test.", e);
        throw new EspmonJMXException(e);
      }
    }
  }


  private static void registerEventType(Configuration config) throws EspmonJMXException {
    Field[] fields = TemperatureEvent.class.getDeclaredFields();
    String[] fieldNames = new String[fields.length];
    Object[] fieldTypes = new Object[fields.length];
    for (int i = 0; i < fields.length; i++ ) {
      Field f = fields[i];
      fieldNames[i] = f.getName();
      fieldTypes[i] = f.getType();
    }

    config.addEventType(TemperatureEvent.class.getSimpleName(), fieldNames, fieldTypes);
  }

  private static void stop(ExecutorService exeSvc) throws EspmonJMXException {
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
    exeSvc.shutdown();
  }
}
