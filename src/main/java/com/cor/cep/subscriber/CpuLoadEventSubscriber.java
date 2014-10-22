package com.cor.cep.subscriber;

import com.cor.cep.event.TemperatureEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since ${date}
 */
@Component
public class CpuLoadEventSubscriber implements StatementSubscriber {

  /** Logger */
  private static Logger LOG = LoggerFactory.getLogger(CpuLoadEventSubscriber.class);

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
    LOG.debug(sb.toString());
  }


}

