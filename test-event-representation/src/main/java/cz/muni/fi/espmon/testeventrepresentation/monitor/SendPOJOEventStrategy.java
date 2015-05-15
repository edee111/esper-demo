package cz.muni.fi.espmon.testeventrepresentation.monitor;

import cz.muni.fi.espmon.testeventrepresentation.event.TemperatureEvent;
import cz.muni.fi.espmon.testeventrepresentation.handler.TemperatureEventHandler;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendPOJOEventStrategy implements SendEventStrategy {

  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    TemperatureEvent tempEve = new TemperatureEvent(temp, date, serverName);
    TemperatureEventHandler.handle(tempEve);
  }
}
