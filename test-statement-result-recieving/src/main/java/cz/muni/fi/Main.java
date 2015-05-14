package cz.muni.fi;

import com.espertech.esper.client.Configuration;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;
import cz.muni.fi.monitor.TemperatureMonitor;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class Main {
  private static final int SERVER_COUNT = 100;
  private static final int DEFAULT_DURATION = 3600;
  private static final String TEMPERATURE_EVENT_XSD_FILE_NAME = "temperature-event.xsd";


  public static void main(String[] args) throws EspmonJMXException {
    int duration = DEFAULT_DURATION;

    if (args.length == 2) {
      try {
        duration = Integer.valueOf(args[0]);
      } catch (Exception e) {}
      try {
        //eventRepresentation = EventRepresentation.valueOfStr(args[1]);// todo reciving result
      } catch (Exception e) {}
    }

    runTest(duration);
  }

  private static void runTest(int durationInSeconds) throws EspmonJMXException {
    Configuration config = new Configuration();
    registerEventType(config);

    /*
     * Not all statements may report metrics: The engine performs certain runtime optimizations sharing resources
     * between similar statements, thereby not reporting on certain statements unless resource sharing
     * is disabled through configuration.
     * Esper refference 16.4.12.1.
     * We want real statement metrics results for purpose of this test. Optimization disabled.
     */
    config.getEngineDefaults().getViewResources().setShareViews(false);

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, 5000, 5000);
    ResultRecievingType type = ResultRecievingType.SUBSCRIBER; //todo listener is not listening


    TemperatureEventHandler.init(config, type);

    runExecution(durationInSeconds);
  }

  private static void runExecution(int durationInSeconds) throws EspmonJMXException {
    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(String.valueOf(i)));
    }

    try {
      Thread.sleep(durationInSeconds * 1000);
    } catch (InterruptedException e) {
      return;
    }

    stop(exSvc);
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

  private static void stop(ExecutorService exSvc) throws EspmonJMXException {
    TemperatureMonitor.stopMonitoring();
    exSvc.shutdown();
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
  }
}
