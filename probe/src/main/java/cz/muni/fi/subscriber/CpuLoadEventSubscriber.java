package cz.muni.fi.subscriber;

import cz.muni.fi.CpuLoad;
import cz.muni.fi.jmx.SimpleAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since ${date}
 */
@Component
public class CpuLoadEventSubscriber extends AbstractSubscriber implements StatementSubscriber {

  @Autowired
  private SimpleAgent simpleAgent;
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
  public void update(Map<String, Double> eventMap) {
    StringBuilder sb = new StringBuilder();
    double avgLoad = eventMap.get("avgLoad");
    sb.append("avgLoad=" + avgLoad);
    log.debug(sb.toString());
    simpleAgent.register(new CpuLoad(avgLoad, new Date()), CpuLoad.class);
  }


}

