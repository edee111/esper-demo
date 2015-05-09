package cz.muni.fi.monitor;

import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.handler.TemperatureEventHandler;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendPOJOEventStrategy implements SendEventStrategy {

  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    TemperatureEvent tempEve = new TemperatureEvent(temp, new Date(), serverName);
    TemperatureEventHandler.handle(tempEve);
  }
}
