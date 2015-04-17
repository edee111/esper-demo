package cz.muni.fi.monitor;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public interface SendEventStrategy {
  public void sendTemperatureEvent(Integer temp, Date date, String serverName);
}
