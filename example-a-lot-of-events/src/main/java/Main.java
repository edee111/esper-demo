import cz.muni.fi.EsperJMXException;
import cz.muni.fi.EsperMetricsMonitor;
import cz.muni.fi.handler.TemperatureEventHandler;
import cz.muni.fi.monitor.TemperatureMonitor;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class Main {

  private static final int SERVER_COUNT = 1000;

  public static void main(String[] args) throws EsperJMXException {
    EsperMetricsMonitor.registerEsperMetricsMonitorFromFile();

    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(i));
    }

    int runInSecondsDuration = 10000;
    try {
      Thread.sleep(runInSecondsDuration * 1000);
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
