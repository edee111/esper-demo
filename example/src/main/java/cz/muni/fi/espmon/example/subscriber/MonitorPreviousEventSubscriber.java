package cz.muni.fi.espmon.example.subscriber;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 19.5.15
 */
public class MonitorPreviousEventSubscriber extends AbstractSubscriber implements StatementSubscriber {
  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatement() {
    return "select irstream avg(temperature) as avg_val, serverName from TemperatureEvent.win:time_batch(5 sec) group by serverName";
  }

  /**
   * Subscriber method called when Esper has detected a pattern match.
   */
  public void update(Map<String, Object> eventMap) {

    // average temp over 10 secs
    Double avg = (Double) eventMap.get("avg_val");
    String serverName = (String) eventMap.get("serverName");

    StringBuilder sb = new StringBuilder();
    sb.append("\n---------------------------------");
    sb.append("\n- [MONITOR - PREVIOUS] Server " + serverName + " average Temp = " + avg);
    sb.append("\n---------------------------------");

    log.debug(sb.toString());
  }
}
