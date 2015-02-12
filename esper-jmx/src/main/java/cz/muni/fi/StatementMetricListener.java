package cz.muni.fi;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.client.UpdateListener;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class StatementMetricListener implements UpdateListener {
  private static StatementMetricListener instance;

  private StatementMetricListener(){}

  public static StatementMetricListener getInstance() {
    if (instance == null) {
      instance = new StatementMetricListener();
    }
    return instance;
  }

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
    key = "statementName";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    key = "cpuTime";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    key = "wallTime";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    key = "numInput";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    key = "numOutputIStream";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    key = "numOutputRStream";
    try {
      System.out.println(key + ": " + b.get(key));
    } catch (PropertyAccessException e) {
      System.out.println(key + " is wrong");
    }
    System.out.println("-------------------");

  }
}

