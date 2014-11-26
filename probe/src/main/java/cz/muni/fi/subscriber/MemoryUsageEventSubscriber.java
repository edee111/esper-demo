package cz.muni.fi.subscriber;

import cz.muni.fi.connector.CpuLoad;
import cz.muni.fi.connector.MemoryUsage;
import cz.muni.fi.connector.SimpleAgent;
import cz.muni.fi.event.CpuLoadEvent;
import cz.muni.fi.event.MemoryUsageEvent;
import cz.muni.fi.event.TemperatureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Component
public class MemoryUsageEventSubscriber extends AbstractSubscriber implements StatementSubscriber {

  @Autowired
  private SimpleAgent simpleAgent;
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
  public void update(Map<String, Double> eventMap) {
    StringBuilder sb = new StringBuilder();
    long avgMemoryUsage = eventMap.get("avgMemoryUsage").longValue();
    sb.append("avgMemoryUsage=" + avgMemoryUsage);
    log.debug(sb.toString());
    simpleAgent.register(new MemoryUsage(avgMemoryUsage, new Date()), MemoryUsageEvent.class);
  }

}
