package cz.muni.fi.subscriber;

import cz.muni.fi.event.TemperatureEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Component
public class MemoryUsageEventSubscriber extends AbstractSubscriber implements StatementSubscriber {

  /**
   * {@inheritDoc}
   */
  public String getStatement() {
    String cpuLoadEventExpression = "select avg(usage) as avgMemoryUsage from MemoryUsageEvent.win:time_batch(10 sec)";
    return cpuLoadEventExpression;
  }

  /**
   * Listener method called when Esper has detected a pattern match.
   */
  public void update(Map<String, TemperatureEvent> eventMap) {
    StringBuilder sb = new StringBuilder();
    sb.append("avgMemoryUsage=" + eventMap.get("avgMemoryUsage"));
    log.debug(sb.toString());
  }

}
