package cz.muni.fi.espmon.teststatementresultrecieving.monitor;

import cz.muni.fi.espmon.teststatementresultrecieving.handler.TemperatureEventHandler;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureMonitor {

  public static final int TEMPERATURE_MIN = 0;
  public static final int TEMPERATURE_MAX = 80;
  public static final int EVENT_PER_SEC_COUNT = 10;

  private String serverName;

  public TemperatureMonitor(String serverName) {
    this.serverName = serverName;
  }

  /**
   * Send given temperature
   *
   * @param temp temperature
   */
  public void sendTemperatureEvent(int temp) {
    Object[] event = {temp, new Date(), serverName};
    TemperatureEventHandler.handle(event);
  }

}
