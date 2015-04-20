package cz.muni.fi;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import cz.muni.fi.jmx.SimpleAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private SimpleAgent simpleAgent;

  private static EngineMetricSubscriber instance;

  private EngineMetricSubscriber() throws EsperJMXException {
    this.simpleAgent = SimpleAgent.getInstance();
  }

  public static EngineMetricSubscriber getInstance() throws EsperJMXException {
    if (instance == null) {
      instance = new EngineMetricSubscriber();
    }
    return instance;
  }

  public void update(com.espertech.esper.client.metric.EngineMetric em) {
    simpleAgent.register(new EngineMetric(em), EngineMetric.class);
  }

}
