package cz.muni.fi;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;
import cz.muni.fi.monitor.TemperatureMonitor;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class MainEventRepresentation {

  private static final int SERVER_COUNT = 100;
  private static final int DEFAULT_DURATION = 3600;
  private static final EventRepresentation DEFAULT_EVENT_REPRESENTATION = EventRepresentation.POJO;
  private static final String TEMPERATURE_EVENT_XSD_FILE_NAME = "temperature-event.xsd";


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
    URL schemaURL = MainEventRepresentation.class.getClassLoader().getResource(MainEventRepresentation.TEMPERATURE_EVENT_XSD_FILE_NAME);

    ConfigurationEventTypeXMLDOM tempCfg = new ConfigurationEventTypeXMLDOM();
    tempCfg.setRootElementName(TemperatureEvent.class.getSimpleName());
    tempCfg.setSchemaResource(schemaURL.toString());

    config.addEventType(TemperatureEvent.class.getSimpleName(), tempCfg);
  }

  private static void runExecution(int durationInSeconds, EventRepresentation repr) throws EspmonJMXException {
    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(i, repr));
    }

    try {
      Thread.sleep(durationInSeconds * 1000);
    } catch (InterruptedException e) {
      return;
    }

    stop(exSvc);
  }

  private static void stop(ExecutorService exSvc) throws EspmonJMXException {
    TemperatureMonitor.stopMonitoring();
    exSvc.shutdown();
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
  }
}
