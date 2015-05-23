package cz.muni.fi.espmonjmx.subscriber;

import cz.muni.fi.espmon.EngineMetric;
import cz.muni.fi.espmonjmx.jmx.JMXAgent;

/**
 * Class subscribing engine metric event
 *
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricSubscriber {
  private JMXAgent jmxAgent = JMXAgent.getInstance();

  /**
   * Update method called by Esper when engine has detected a pattern match.
   *
   * @param em passed engine metric event
   */
  public void update(com.espertech.esper.client.metric.EngineMetric em) {
    jmxAgent.register(new EngineMetric(em));
  }

}
