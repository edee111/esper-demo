package cz.muni.fi;

import cz.muni.fi.espmon.StatementMetric;
import cz.muni.fi.jmx.JMXAgent;

/**
 * Class aggregates statement metrics by its timestamp.
 *
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class StatementMetricAggregator {

  private StatementMetric aggSM;
  private JMXAgent jmxAgent = JMXAgent.getInstance();

  /**
   * Create class instance and init aggregated statement metric
   */
  public StatementMetricAggregator() {
    this.aggSM = new StatementMetric();
    this.aggSM.setStatementName("TotalStatementMetrics");
  }


  /**
   * Add new statement metric to aggregation and register total metrics if timestamp differs
   *
   * @param sm statement metric to aggregate
   */
  public synchronized void addStatementMetric(StatementMetric sm) {
    boolean metricIsFromTheSameInterval = aggSM.getTimestamp() == sm.getTimestamp();
    if (metricIsFromTheSameInterval) {
      doAggregate(sm);
    }
    else {
      jmxAgent.register(aggSM);
      initAggSM(sm);
    }
  }

  /**
   * Aggregate internal statement metric and passed statement metric
   * @param sm next statement metric
   */
  private synchronized void doAggregate(StatementMetric sm) {
    this.aggSM.setCpuTime(this.aggSM.getCpuTime() + sm.getCpuTime());
    this.aggSM.setWallTime(this.aggSM.getWallTime() + sm.getWallTime());
    this.aggSM.setNumInput(this.aggSM.getNumInput() + sm.getNumInput());
    this.aggSM.setNumOutputIStream(this.aggSM.getNumOutputIStream() + sm.getNumOutputIStream());
    this.aggSM.setNumOutputRStream(this.aggSM.getNumOutputRStream() + sm.getNumOutputRStream());
  }

  /**
   * Initialize internal aggregated statement metric by copying of passed statement metrics
   * @param sm
   */
  private synchronized void initAggSM(StatementMetric sm) {
    aggSM.setTimestamp(sm.getTimestamp());
    aggSM.setCpuTime(sm.getCpuTime());
    aggSM.setWallTime(sm.getWallTime());
    aggSM.setNumInput(sm.getNumInput());
    aggSM.setNumOutputIStream(sm.getNumOutputIStream());
    aggSM.setNumOutputRStream(sm.getNumOutputRStream());
  }
}
