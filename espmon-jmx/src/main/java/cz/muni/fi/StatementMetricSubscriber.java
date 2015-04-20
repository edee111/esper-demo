package cz.muni.fi;

import cz.muni.fi.jmx.SimpleJMXAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class StatementMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private SimpleJMXAgent simpleJMXAgent;
  private StatementMetricAggregator sma;

  private static StatementMetricSubscriber instance;

  private StatementMetricSubscriber() throws EsperJMXException {
    this.simpleJMXAgent = SimpleJMXAgent.getInstance();
    this.sma = new StatementMetricAggregator();
  }

  public static StatementMetricSubscriber getInstance() throws EsperJMXException {
    if (instance == null) {
      instance = new StatementMetricSubscriber();
    }
    return instance;
  }

  public void update(com.espertech.esper.client.metric.StatementMetric sm) {
    StatementMetric smMBean = new StatementMetric(sm);
    simpleJMXAgent.register(smMBean);
    sma.addNewStatementMetric(this.simpleJMXAgent, smMBean);
  }
}

