package cz.muni.fi.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import cz.muni.fi.event.TemperatureEvent;
import cz.muni.fi.subscriber.BaseSubscriber;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public abstract class AbstractListener implements UpdateListener {

  protected BaseSubscriber baseSubscriber;


  @Override
  public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    if (newEvents.length != 1) {
      throw new IllegalArgumentException("Unexpected number of new events: " + newEvents.length);
    }

    EventBean b = newEvents[0];
    Map<String, Object> eventMap = new HashMap<>(3);
    Field[] fields = TemperatureEvent.class.getDeclaredFields();
    for (Field f : fields) {
      eventMap.put(f.getName(), b.get(f.getName()));
    }

    baseSubscriber.update(eventMap);
  }
}
