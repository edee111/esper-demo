package cz.muni.fi;

import cz.muni.fi.jmx.JMXAgent;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EngineMetricSubscriber {
  private JMXAgent jmxAgent;

  private static EngineMetricSubscriber instance;

  private EngineMetricSubscriber() throws EspmonJMXException {
    this.jmxAgent = jmxAgent.getInstance();
  }

  public static EngineMetricSubscriber getInstance() throws EspmonJMXException {
    if (instance == null) {
      instance = new EngineMetricSubscriber();
    }
    return instance;
  }

  /**
   * Update method called by Esper engine.
   *
   * @param em passed engine metric event
   */
  public void update(com.espertech.esper.client.metric.EngineMetric em) {
    jmxAgent.register(new EngineMetric(em));
  }

}
