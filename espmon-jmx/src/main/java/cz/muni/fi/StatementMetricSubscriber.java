package cz.muni.fi;

import cz.muni.fi.jmx.JMXAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class StatementMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private JMXAgent jmxAgent;
  private StatementMetricAggregator sma;

  private static StatementMetricSubscriber instance;

  private StatementMetricSubscriber() throws EsperJMXException {
    this.jmxAgent = JMXAgent.getInstance();
    this.sma = new StatementMetricAggregator(jmxAgent);
  }

  public static StatementMetricSubscriber getInstance() throws EsperJMXException {
    if (instance == null) {
      instance = new StatementMetricSubscriber();
    }
    return instance;
  }

  public void update(com.espertech.esper.client.metric.StatementMetric sm) {
    StatementMetric smMBean = new StatementMetric(sm);
    jmxAgent.register(smMBean);
    sma.addNewStatementMetric(smMBean);
  }
}

