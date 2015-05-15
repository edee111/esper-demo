package cz.muni.fi.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
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
public abstract class BaseListener implements UpdateListener, StatementListener {

  protected BaseSubscriber baseSubscriber;

  @Override
  public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    for (EventBean b : newEvents) {
      Map<String, Object> eventMap = new HashMap<>(TemperatureEvent.class.getDeclaredFields().length);

      String[] statementResultNames = baseSubscriber.getStatementResultNames();
      for (String resultName : statementResultNames) {
        eventMap.put(resultName, b.get(resultName));
      }

      baseSubscriber.update(eventMap);
    }
  }

  @Override
  public String getStatement() {
    return baseSubscriber.getStatement();
  }
}
