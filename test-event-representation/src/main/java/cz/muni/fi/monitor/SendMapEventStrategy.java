package cz.muni.fi.monitor;

import cz.muni.fi.handler.TemperatureEventHandler;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendMapEventStrategy implements SendEventStrategy {

  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    HashMap<String, Object> mapEvent = new HashMap();
    mapEvent.put("temperature", temp);
    mapEvent.put("timeOfReading", new Date());
    mapEvent.put("serverName", serverName);

    TemperatureEventHandler.handle(mapEvent);
  }
}
