import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import cz.muni.fi.EsperJMXException;
import cz.muni.fi.EsperMetricsMonitor;
import cz.muni.fi.EventRepresentation;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;
import cz.muni.fi.monitor.TemperatureMonitor;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class Main {

  private static final int SERVER_COUNT = 1000;
  private static final int DEFAULT_DURATION = 3600;
  private static final EventRepresentation DEFAULT_EVENT_REPRESENTATION = EventRepresentation.POJO;


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

    runTest(duration, eventRepresentation);
  }

  private static void runTest(int durationInSeconds, EventRepresentation repr) throws EsperJMXException {
    Configuration config = new Configuration();

    switch (repr) {
      case POJO: setupPOJOTest(config);
    }

    EsperMetricsMonitor.registerEsperMetricsMonitorWithValues(config, 5000, 5000);
    TemperatureEventHandler.init(config);

    runExecution(durationInSeconds);
  }

  private static void setupPOJOTest(Configuration config) throws EsperJMXException {
    config.addEventType(TemperatureEvent.class);
  }

  private static void runExecution(int durationInSeconds) throws EsperJMXException {
    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(i));
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
