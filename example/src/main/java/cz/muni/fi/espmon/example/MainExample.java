package cz.muni.fi.espmon.example;

import com.espertech.esper.client.Configuration;
import cz.muni.fi.espmonjmx.EsperMetricsMonitor;
import cz.muni.fi.espmonjmx.EspmonJMXException;
import cz.muni.fi.espmon.example.event.TemperatureEvent;
import cz.muni.fi.espmon.example.handler.TemperatureEventHandler;
import cz.muni.fi.espmon.example.monitor.TemperatureMonitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class MainExample {

  private static final int DEFAULT_SERVER_COUNT = 10;
  private static final int DEFAULT_DURATION = 3600;

  public static void main(String[] args) throws EspmonJMXException {
    int duration = DEFAULT_DURATION;
    int serverCount = DEFAULT_SERVER_COUNT;
    Integer portNumber = null;

    if (args.length == 3) {
      duration = Integer.valueOf(args[0]);
      serverCount = Integer.valueOf(args[1]);
      portNumber = Integer.valueOf(args[2]);
    }
    else if (args.length != 0) {
      System.out.println("Incorrect number of arguments. Arguments are: durationInSeconds serverCount portNumber");
      return;
    }

    runTest(duration, serverCount, portNumber);
  }

  private static void runTest(int durationInSeconds, Integer serverCount, Integer portNumber) throws EspmonJMXException {
    Configuration config = new Configuration();

    setupPOJOTest(config);
    /*
     * Not all statements may report metrics: The engine performs certain runtime optimizations sharing resources
     * between similar statements, thereby not reporting on certain statements unless resource sharing
     * is disabled through configuration.
     * Esper refference 16.4.12.1.
     * We want real statement metrics results for purpose of this test. Optimization disabled.
     */
    config.getEngineDefaults().getViewResources().setShareViews(false);

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, 5000, 5000, portNumber);
    TemperatureEventHandler.init(config);

    runExecution(durationInSeconds, serverCount);
  }

  private static void setupPOJOTest(Configuration config) throws EspmonJMXException {
    config.addEventType(TemperatureEvent.class);
  }


  private static void runExecution(int durationInSeconds, int serverCount) throws EspmonJMXException {
    ExecutorService exSvc = Executors.newFixedThreadPool(serverCount);

    for (int i = 1; i <= serverCount; i++) {
      exSvc.execute(new TemperatureMonitor(i));
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
