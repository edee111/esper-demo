package cz.muni.fi.monitor;

import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureMonitor implements Runnable {

  private static AtomicBoolean isMonitoringRunning = new AtomicBoolean(true);
  private static final int TEMPERATURE_MIN = 0;
  private static final int TEMPERATURE_MAX = 80;
  private static final int EVENT_PER_SEC_COUNT = 10;

  private String serverName;

  public TemperatureMonitor(int serverNumber) {
    this.serverName = String.valueOf(serverNumber);
  }

  @Override
  public void run() {
    Random random = new Random();
    int randomRange = TEMPERATURE_MAX - TEMPERATURE_MIN;
    while (isMonitoringRunning.get()) {
      int temp = random.nextInt(randomRange);
      sendTemperatureEvent(TEMPERATURE_MIN + temp);

      try {
        Thread.sleep(1000 / EVENT_PER_SEC_COUNT);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void sendTemperatureEvent(int temp) {
    TemperatureEvent tempEve = new TemperatureEvent(temp, new Date(), serverName);
    TemperatureEventHandler.handle(tempEve);
  }

  public static void stopMonitoring() {
    isMonitoringRunning.set(false);
  }
}
