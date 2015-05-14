package cz.muni.fi.monitor;

import cz.muni.fi.EventRepresentation;

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
  private SendEventStrategy sendEventStrat;


  public TemperatureMonitor(int serverNumber, EventRepresentation repr) {
    this.serverName = String.valueOf(serverNumber);
    synchronized (repr) {
      switch (repr) {
        case POJO:
          this.sendEventStrat = new SendPOJOEventStrategy();
          break;
        case MAP:
          this.sendEventStrat = new SendMapEventStrategy();
          break;
        case ARRAY:
          this.sendEventStrat = new SendArrayEventStrategy();
          break;
        case XML:
          this.sendEventStrat = new SendXMLEventStrategy();
          break;
      }
    }
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
    sendEventStrat.sendTemperatureEvent(temp, new Date(), this.serverName);
  }

  public static void stopMonitoring() {
    isMonitoringRunning.set(false);
  }
}
