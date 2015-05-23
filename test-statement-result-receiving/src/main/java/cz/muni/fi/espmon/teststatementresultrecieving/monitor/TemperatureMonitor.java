package cz.muni.fi.espmon.teststatementresultrecieving.monitor;

import cz.muni.fi.espmon.teststatementresultrecieving.event.TemperatureEvent;
import cz.muni.fi.espmon.teststatementresultrecieving.handler.TemperatureEventHandler;

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

  public TemperatureMonitor(String serverName) {
    this.serverName = serverName;
  }

  @Override
  public void run() {
    Random random = new Random();
    int randomRange = TEMPERATURE_MAX - TEMPERATURE_MIN;
    int sleepTime = 1000 / EVENT_PER_SEC_COUNT;
    while (isMonitoringRunning.get()) {
      int temp = random.nextInt(randomRange);
      sendTemperatureEvent(TEMPERATURE_MIN + temp);

      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Send given temperature
   *
   * @param temp temperature
   */
  private void sendTemperatureEvent(int temp) {
    TemperatureEvent event = new TemperatureEvent(temp, new Date(), serverName);
    TemperatureEventHandler.handle(event);
  }

  public static void stopMonitoring() {
    isMonitoringRunning.set(false);
  }
}
