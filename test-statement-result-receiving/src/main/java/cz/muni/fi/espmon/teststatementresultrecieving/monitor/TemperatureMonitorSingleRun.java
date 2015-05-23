package cz.muni.fi.espmon.teststatementresultrecieving.monitor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Eduard Tomek
 * @since 22.5.15
 */
public class TemperatureMonitorSingleRun implements Callable<Object> {
  private List<TemperatureMonitor> monitors;
  private Random random = new Random();
  private int randomRange = TemperatureMonitor.TEMPERATURE_MAX - TemperatureMonitor.TEMPERATURE_MIN;

  public TemperatureMonitorSingleRun(List<TemperatureMonitor> monitors) {
    this.monitors = monitors;
  }

  /**
   * Run single teperature check over all monitors in this run
   * @throws Exception
   */
  @Override
  public Object call() throws Exception {
    for (TemperatureMonitor m : monitors) {
      int temp = random.nextInt(randomRange);
      m.sendTemperatureEvent(temp);
    }
    return null;
  }
}
