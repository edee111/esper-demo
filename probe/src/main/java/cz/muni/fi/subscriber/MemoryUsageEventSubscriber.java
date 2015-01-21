package cz.muni.fi.subscriber;

import cz.muni.fi.MemoryUsage;
import cz.muni.fi.jmx.SimpleAgent;
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
    String cpuLoadEventExpression = "select avg(usage) as avgMemoryUsage, avg(totalMemory) as avgTotalMemory from MemoryUsageEvent.win:time_batch(10 sec)";
    return cpuLoadEventExpression;
  }

  /**
   * Listener method called when Esper has detected a pattern match.
   */
  public void update(Map<String, Double> eventMap) {
    StringBuilder sb = new StringBuilder();
    long avgMemoryUsage = eventMap.get("avgMemoryUsage").longValue();
    long totalMemory = eventMap.get("avgTotalMemory").longValue();
    sb.append("totalMemory=" + totalMemory);
    sb.append(", avgMemoryUsage=" + avgMemoryUsage);
    log.debug(sb.toString());
    simpleAgent.register(new MemoryUsage(totalMemory, avgMemoryUsage, new Date()), MemoryUsage.class);
  }

}
