package cz.muni.fi.subscriber;


import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class MonitorEventSubscriber extends AbstractSubscriber implements StatementSubscriber {
  /**
   * {@inheritDoc}
   */
  public String getStatement() {
    return "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)";
  }

  /**
   * Listener method called when Esper has detected a pattern match.
   */
  public void update(Map<String, Double> eventMap) {

    // average temp over 10 secs
    Double avg = (Double) eventMap.get("avg_val");

    StringBuilder sb = new StringBuilder();
    sb.append("---------------------------------");
    sb.append("\n- [MONITOR] Average Temp = " + avg);
    sb.append("\n---------------------------------");

    log.debug(sb.toString());
  }
}
