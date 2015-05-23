package cz.muni.fi.espmon.testeventrepresentation.monitor;

import cz.muni.fi.espmon.testeventrepresentation.handler.TemperatureEventHandler;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendMapEventStrategy implements SendEventStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    HashMap<String, Object> mapEvent = new HashMap();
    mapEvent.put("temperature", temp);
    mapEvent.put("timeOfReading", date);
    mapEvent.put("serverName", serverName);

    TemperatureEventHandler.handle(mapEvent);
  }
}
