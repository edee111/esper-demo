package cz.muni.fi;

import cz.muni.fi.jmx.JMXAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private JMXAgent jmxAgent;

  private static EngineMetricSubscriber instance;

  private EngineMetricSubscriber() throws EsperJMXException {
    this.jmxAgent = jmxAgent.getInstance();
  }

  public static EngineMetricSubscriber getInstance() throws EsperJMXException {
    if (instance == null) {
      instance = new EngineMetricSubscriber();
    }
    return instance;
  }

  public void update(com.espertech.esper.client.metric.EngineMetric em) {
    jmxAgent.register(new EngineMetric(em));
  }

}
