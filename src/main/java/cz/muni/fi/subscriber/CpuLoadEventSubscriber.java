package cz.muni.fi.subscriber;

import cz.muni.fi.event.TemperatureEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since ${date}
 */
@Component
public class CpuLoadEventSubscriber extends AbstractSubscriber implements StatementSubscriber {

  /**
   * {@inheritDoc}
   */
  public String getStatement() {
    String cpuLoadEventExpression = "select avg(load) as avgLoad from CpuLoadEvent.win:time_batch(10 sec)";
    return cpuLoadEventExpression;
  }

  /**
   * Listener method called when Esper has detected a pattern match.
   */
  public void update(Map<String, TemperatureEvent> eventMap) {
    StringBuilder sb = new StringBuilder();
    sb.append("avgLoad=" + eventMap.get("avgLoad"));
    log.debug(sb.toString());
  }


}

