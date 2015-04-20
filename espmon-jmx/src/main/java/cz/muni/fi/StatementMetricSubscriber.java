package cz.muni.fi;

import com.espertech.esper.client.EventBean;
import cz.muni.fi.jmx.SimpleAgent;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class StatementMetricSubscriber {
  protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

  private SimpleAgent simpleAgent;
  private StatementMetricAggregator sma;

  private static StatementMetricSubscriber instance;

  private StatementMetricSubscriber() throws EsperJMXException {
    this.simpleAgent = SimpleAgent.getInstance();
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
    simpleAgent.register(smMBean, StatementMetric.class);
    sma.addNewStatementMetric(this.simpleAgent, smMBean);
  }
}

