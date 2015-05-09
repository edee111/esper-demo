package cz.muni.fi;

import com.espertech.esper.client.Configuration;
import cz.muni.fi.monitor.TemperatureMonitor;

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

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, 5000, 5000);
    //TemperatureEventHandler.init(config, repr);

    //runExecution(durationInSeconds);
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

  private static void stop(ExecutorService exSvc) throws EspmonJMXException {
    TemperatureMonitor.stopMonitoring();
    exSvc.shutdown();
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
  }
}
