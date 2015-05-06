package cz.muni.fi;

import cz.muni.fi.jmx.JMXAgent;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class StatementMetricAggregator {
  private StatementMetric aggSM;

  private JMXAgent jmxAgent;

  public StatementMetricAggregator(JMXAgent jmxAgent) {
    this.jmxAgent = jmxAgent;
    this.aggSM = new StatementMetric();
    this.aggSM.setStatementName("TotalStatementMetrics");
  }

  public synchronized void addNewStatementMetric(StatementMetric sm) {
    boolean metricIsFromTheSameInterval = aggSM.getTimestamp() == sm.getTimestamp();
    if (metricIsFromTheSameInterval) {
      doAggregate(sm);
    }
    else {
      jmxAgent.register(aggSM);
      initAggSM(sm);
    }
  }

  private synchronized void doAggregate(StatementMetric sm) {
    this.aggSM.setCpuTime(this.aggSM.getCpuTime() + sm.getCpuTime());
    this.aggSM.setWallTime(this.aggSM.getWallTime() + sm.getWallTime());
    this.aggSM.setNumInput(this.aggSM.getNumInput() + sm.getNumInput());
    this.aggSM.setNumOutputIStream(this.aggSM.getNumOutputIStream() + sm.getNumOutputIStream());
    this.aggSM.setNumOutputRStream(this.aggSM.getNumOutputRStream() + sm.getNumOutputRStream());
  }

  private synchronized void initAggSM(StatementMetric sm) {
    aggSM.setTimestamp(sm.getTimestamp());
    aggSM.setCpuTime(sm.getCpuTime());
    aggSM.setWallTime(sm.getWallTime());
    aggSM.setNumInput(sm.getNumInput());
    aggSM.setNumOutputIStream(sm.getNumOutputIStream());
    aggSM.setNumOutputRStream(sm.getNumOutputRStream());
  }
}
