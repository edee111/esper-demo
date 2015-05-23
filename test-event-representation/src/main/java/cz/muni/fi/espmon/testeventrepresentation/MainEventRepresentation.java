package cz.muni.fi.espmon.testeventrepresentation;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import cz.muni.fi.espmon.testeventrepresentation.event.TemperatureEvent;
import cz.muni.fi.espmon.testeventrepresentation.handler.TemperatureEventHandler;
import cz.muni.fi.espmon.testeventrepresentation.monitor.TemperatureMonitor;
import cz.muni.fi.espmon.testeventrepresentation.monitor.TemperatureMonitorSingleRun;
import cz.muni.fi.espmonjmx.EsperMetricsMonitor;
import cz.muni.fi.espmonjmx.EspmonJMXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class MainEventRepresentation {

  private static final int SERVER_COUNT = 2000;
  private static final int SERVER_PER_THREAD_COUNT = 500;
  private static final int DEFAULT_DURATION = 3600;
  private static final EventRepresentation DEFAULT_EVENT_REPRESENTATION = EventRepresentation.POJO;
  private static final String TEMPERATURE_EVENT_XSD_FILE_NAME = "temperature-event.xsd";

  private static final Logger log = LoggerFactory.getLogger(MainEventRepresentation.class);

  /**
   * Main method of module
   *
   * @param args arguments, which are durationInSecods, eventRepresenation, portNumber(optional)
   * @throws EspmonJMXException
   */
  public static void main(String[] args) throws EspmonJMXException {
    EventRepresentation eventRepresentation = DEFAULT_EVENT_REPRESENTATION;
    int duration = DEFAULT_DURATION;
    Integer portNumber = null;

    if (args.length >= 2) {
        duration = Integer.valueOf(args[0]);
      try {
        eventRepresentation = EventRepresentation.valueOfStr(args[1]);
      } catch (Exception e) {}
    }
    if (args.length == 3) {
      portNumber = Integer.valueOf(args[2]);
    }

    runTest(duration, eventRepresentation, portNumber);
  }

  private static void runTest(int durationInSeconds, EventRepresentation repr, Integer portNumber) throws EspmonJMXException {
    Configuration config = new Configuration();

    switch (repr) {
      case POJO: setupPOJOTest(config); break;
      case MAP: setupMapTest(config); break;
      case ARRAY: setupArrayTest(config); break;
      case XML: setupXMLTest(config); break;
    }

    /*
     * Not all statements may report metrics: The engine performs certain runtime optimizations sharing resources
     * between similar statements, thereby not reporting on certain statements unless resource sharing
     * is disabled through configuration.
     * Esper refference 16.4.12.1.
     * We want real statement metrics results for purpose of this test. Optimization disabled.
     */
    config.getEngineDefaults().getViewResources().setShareViews(false);

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, 5000, 5000, portNumber);
    TemperatureEventHandler.init(config, repr);

    runExecution(durationInSeconds, repr);
  }

  /**
   * Run execution of test for given duration and event representation
   *
   * @param durationInSeconds duration of test
   * @param repr used event representation
   * @throws EspmonJMXException
   */
  private static void runExecution(int durationInSeconds, EventRepresentation repr) throws EspmonJMXException {
    long runEndTime = durationInSeconds * 1000 + System.currentTimeMillis();
    int defaultSleepTime = 1000 / TemperatureMonitor.EVENT_PER_SEC_COUNT;
    Collection<TemperatureMonitorSingleRun> runs = prepareRuns(repr);
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

  /**
   * Prepare temperature monitors
   *
   * @param repr used event representation
   * @return collection of callable monitors
   */
  private static Collection<TemperatureMonitorSingleRun> prepareRuns(EventRepresentation repr) {
    Collection<TemperatureMonitorSingleRun> runs = new ArrayList<>();
    int i;
    for (i = 1; i < SERVER_COUNT; i += SERVER_PER_THREAD_COUNT) {
      List<TemperatureMonitor> monitorsForRun = createMonitors(i, i + SERVER_PER_THREAD_COUNT, repr);
      runs.add(new TemperatureMonitorSingleRun(monitorsForRun));
    }

    return runs;
  }

  private static List<TemperatureMonitor> createMonitors(int from, int to, EventRepresentation repr) {
    List<TemperatureMonitor> monitors = new ArrayList<>();
    for (int i = from; i < to; i++) {
      monitors.add(new TemperatureMonitor(i, repr));
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

  private static void setupPOJOTest(Configuration config) throws EspmonJMXException {
    config.addEventType(TemperatureEvent.class);
  }

  private static void setupMapTest(Configuration config) throws EspmonJMXException {
    Map<String, Object> def = new HashMap();
    Field[] fields = TemperatureEvent.class.getDeclaredFields();
    for (Field f : fields) {
      def.put(f.getName(), f.getType());
    }

    config.addEventType(TemperatureEvent.class.getSimpleName(), def);
  }

  private static void setupArrayTest(Configuration config) throws EspmonJMXException {
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

  private static void setupXMLTest(Configuration config) throws EspmonJMXException {
    URL schemaURL = MainEventRepresentation.class.getClassLoader()
            .getResource(MainEventRepresentation.TEMPERATURE_EVENT_XSD_FILE_NAME);

    ConfigurationEventTypeXMLDOM tempCfg = new ConfigurationEventTypeXMLDOM();
    tempCfg.setRootElementName(TemperatureEvent.class.getSimpleName());
    tempCfg.setSchemaResource(schemaURL.toString());

    config.addEventType(TemperatureEvent.class.getSimpleName(), tempCfg);
  }

  private static void stop(ExecutorService exSvc) throws EspmonJMXException {
    exSvc.shutdown();
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
  }
}
