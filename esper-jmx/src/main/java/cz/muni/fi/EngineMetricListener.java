package cz.muni.fi;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.sun.org.apache.xpath.internal.SourceTree;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricListener implements UpdateListener {
  public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    for (EventBean b : newEvents) {
      printInfo(b);
    }
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
