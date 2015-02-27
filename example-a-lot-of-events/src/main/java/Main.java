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

  private static final int SERVER_COUNT = 1;

  public static void main(String[] args) throws EsperJMXException {
    EsperMetricsMonitor.registerEsperMetricsMonitorWithValues(5000, 5000);

    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(i));
    }

    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      return;
    }

    TemperatureMonitor.stopMonitoring();

    //udelat neco ve stylu sberna teplot v serverovne - vice vlaken - kazde vlakno jako jeden server chrli teplotu systemu
    //ktere jsou eventy a bude jich hodne
  }
}
