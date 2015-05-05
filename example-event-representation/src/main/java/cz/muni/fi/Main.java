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
public class Main {

  private static final int SERVER_COUNT = 100;
  private static final int DEFAULT_DURATION = 3600;
  private static final EventRepresentation DEFAULT_EVENT_REPRESENTATION = EventRepresentation.POJO;
  private static final String TEMPERATURE_EVENT_XSD_FILE_NAME = "temperature-event.xsd";


  public static void main(String[] args) throws EsperJMXException {
    EventRepresentation eventRepresentation = DEFAULT_EVENT_REPRESENTATION;
    int duration = DEFAULT_DURATION;

    if (args.length == 2) {
      try {
        duration = Integer.valueOf(args[0]);
      } catch (Exception e) {}
      try {
        eventRepresentation = EventRepresentation.valueOfStr(args[1]);
      } catch (Exception e) {}
    }

    //todo hack
    eventRepresentation = EventRepresentation.XML;
    runTest(duration, eventRepresentation);
  }

  private static void runTest(int durationInSeconds, EventRepresentation repr) throws EsperJMXException {
    Configuration config = new Configuration();

    switch (repr) {
      case POJO: setupPOJOTest(config); break;
      case MAP: setupMapTest(config); break;
      case ARRAY: setupArrayTest(config); break;
      case XML: setupXMLTest(config); break;
    }

    EsperMetricsMonitor.registerEsperMetricsMonitorWithValues(config, 5000, 5000);
    TemperatureEventHandler.init(config, repr);

    runExecution(durationInSeconds, repr);
  }

  private static void setupPOJOTest(Configuration config) throws EsperJMXException {
    config.addEventType(TemperatureEvent.class);
  }

  private static void setupMapTest(Configuration config) throws EsperJMXException {
    Map<String, Object> def = new HashMap();
    Field[] fields = TemperatureEvent.class.getDeclaredFields();
    for (Field f : fields) {
      def.put(f.getName(), f.getType());
    }

    config.addEventType(TemperatureEvent.class.getSimpleName(), def);
  }

  private static void setupArrayTest(Configuration config) throws EsperJMXException {
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

  private static void setupXMLTest(Configuration config) throws EsperJMXException {
    URL schemaURL = Main.class.getClassLoader().getResource(Main.TEMPERATURE_EVENT_XSD_FILE_NAME);

    ConfigurationEventTypeXMLDOM tempCfg = new ConfigurationEventTypeXMLDOM();
    tempCfg.setRootElementName(TemperatureEvent.class.getSimpleName());
    tempCfg.setSchemaResource(schemaURL.toString());

    config.addEventType(TemperatureEvent.class.getSimpleName(), tempCfg);
  }

  private static void runExecution(int durationInSeconds, EventRepresentation repr) throws EsperJMXException {
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

  private static void stop(ExecutorService exSvc) throws EsperJMXException {
    TemperatureMonitor.stopMonitoring();
    exSvc.shutdown();
    EsperMetricsMonitor.stop();
  }
}
