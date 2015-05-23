package cz.muni.fi.espmon.testeventrepresentation.monitor;

import cz.muni.fi.espmon.testeventrepresentation.EventRepresentation;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureMonitor {

  public static final int TEMPERATURE_MIN = 0;
  public static final int TEMPERATURE_MAX = 80;
  public static final int EVENT_PER_SEC_COUNT = 2;

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

  public void sendTemperatureEvent(int temp) {
    sendEventStrat.sendTemperatureEvent(temp, new Date(), this.serverName);
  }
}
