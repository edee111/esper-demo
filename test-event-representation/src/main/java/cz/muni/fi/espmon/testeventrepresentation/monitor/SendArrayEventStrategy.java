package cz.muni.fi.espmon.testeventrepresentation.monitor;

import cz.muni.fi.espmon.testeventrepresentation.handler.TemperatureEventHandler;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendArrayEventStrategy  implements SendEventStrategy {

  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    Object[] event = {temp, date, serverName};
    TemperatureEventHandler.handle(event);
  }
}
