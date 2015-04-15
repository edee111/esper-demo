package cz.muni.fi.monitor;

import cz.muni.fi.EventRepresentation;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;

import java.util.Date;
import java.util.HashMap;
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
  private TemperatureEventHandler temperatureEventHandler;
  private EventRepresentation representation;


  public TemperatureMonitor(int serverNumber, EventRepresentation repr) {
    this.serverName = String.valueOf(serverNumber);
    this.temperatureEventHandler = TemperatureEventHandler.getInstance();
    this.representation = repr;
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
    switch (representation) {
      case POJO:
        TemperatureEvent tempEve = new TemperatureEvent(TEMPERATURE_MIN + temp, new Date(), serverName);
        temperatureEventHandler.handle(tempEve);
        break;
      case MAP:
        HashMap<String, Object> mapEvent = new HashMap();
        mapEvent.put("temperature", temp);
        mapEvent.put("timeOfReading", new Date());
        mapEvent.put("serverName", serverName);

        temperatureEventHandler.handle(mapEvent);
        break;
      case ARRAY:
        Object[] event = {temp, new Date(), serverName};
        temperatureEventHandler.handle(event);
    }
  }

  public static void stopMonitoring() {
    isMonitoringRunning.set(false);
  }
}
