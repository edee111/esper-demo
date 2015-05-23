package cz.muni.fi.espmon.testeventrepresentation.monitor;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public interface SendEventStrategy {

  /**
   * Create and send teperature event
   *
   * @param temp temperature
   * @param date timestamp
   * @param serverName name of server with given temperature
   */
  public void sendTemperatureEvent(Integer temp, Date date, String serverName);
}
