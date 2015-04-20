package cz.muni.fi;

import cz.muni.fi.jmx.SimpleJMXAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private SimpleJMXAgent simpleJMXAgent;

  private static EngineMetricSubscriber instance;

  private EngineMetricSubscriber() throws EsperJMXException {
    this.simpleJMXAgent = SimpleJMXAgent.getInstance();
  }

  public static EngineMetricSubscriber getInstance() throws EsperJMXException {
    if (instance == null) {
      instance = new EngineMetricSubscriber();
    }
    return instance;
  }

  public void update(com.espertech.esper.client.metric.EngineMetric em) {
    simpleJMXAgent.register(new EngineMetric(em));
  }

}
