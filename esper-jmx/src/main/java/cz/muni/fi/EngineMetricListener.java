package cz.muni.fi;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import cz.muni.fi.jmx.SimpleAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricListener implements UpdateListener {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private SimpleAgent simpleAgent;

  private static EngineMetricListener instance;

  private EngineMetricListener(){
    this.simpleAgent = SimpleAgent.getInstance();
  }

  public static EngineMetricListener getInstance() {
    if (instance == null) {
      instance = new EngineMetricListener();
    }
    return instance;
  }

  @Override
  public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    if (newEvents.length != 1) {
      throw new IllegalArgumentException("Unexpected number of new events: " + newEvents.length);
    }
    EventBean b = newEvents[0];

    EngineMetric em = new EngineMetric();
    em.setTimestamp((long) b.get("timestamp"));
    em.setInputCount((long) b.get("inputCount"));
    em.setInputCountDelta((long) b.get("inputCountDelta"));
    em.setScheduleDepth((long) b.get("scheduleDepth"));

    simpleAgent.register(em, EngineMetric.class);
  }

  private void printInfo(EventBean b) {
    System.out.println("printInfo: ");
    System.out.println(b.getEventType());
    System.out.println(b.getUnderlying());
    String key = "timestamp";
    System.out.println(key + ": " + b.get(key));
    key = "inputCount";
    System.out.println(key + ": " + b.get(key));
    key = "inputCountDelta";
    System.out.println(key + ": " + b.get(key));
    key = "scheduleDepth";
    System.out.println(key + ": " + b.get(key));
    System.out.println("-------------------");

  }
}
