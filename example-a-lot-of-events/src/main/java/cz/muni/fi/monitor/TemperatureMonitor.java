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
  private static final int TEMPERATURE_MIN = 20;
  private static final int TEMPERATURE_MAX = 100;
  private static final int EVENT_PER_SEC_COUNT = 100;

  private int serverNumber;
  private TemperatureEventHandler temperatureEventHandler;


  public TemperatureMonitor(int serverNumber) {
    this.serverNumber = serverNumber;
    temperatureEventHandler = TemperatureEventHandler.getInstance();
  }

  @Override
  public void run() {
    Random random = new Random();
    int randomRange = TEMPERATURE_MAX - TEMPERATURE_MIN;
    while (isMonitoringRunning.get()) {
      int temp = random.nextInt(randomRange);
      TemperatureEvent tempEve = new TemperatureEvent(TEMPERATURE_MIN + temp, new Date());
      temperatureEventHandler.handle(tempEve);
      try {
        Thread.sleep(1000 / EVENT_PER_SEC_COUNT);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void stopMonitoring() {
    isMonitoringRunning.set(false);
  }
}
