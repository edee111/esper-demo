package cz.muni.fi;

import cz.muni.fi.jmx.SimpleAgent;

import javax.swing.plaf.nimbus.State;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class StatementMetricAggregator {
  private StatementMetric aggSM;

  public synchronized void addNewStatementMetric(SimpleAgent simpleAgent, StatementMetric sm) {
    if (aggSM == null) {
      aggSM = new StatementMetric();
      updateAggSM(sm);
      aggSM.setStatementName("Aggregated statements");
    }
    else if (aggSM.getTimestamp() == sm.getTimestamp()) {
      doAggregate(sm);
    }
    else {
      simpleAgent.register(aggSM, StatementMetric.class);
      updateAggSM(sm);
      aggSM.setStatementName("Aggregated statements");
    }
  }

  private synchronized void doAggregate(StatementMetric sm) {
    this.aggSM.setCpuTime(this.aggSM.getCpuTime() + sm.getCpuTime());
    this.aggSM.setWallTime(this.aggSM.getWallTime() + sm.getWallTime());
    this.aggSM.setNumInput(this.aggSM.getNumInput() + sm.getNumInput());
    this.aggSM.setNumOutputIStream(this.aggSM.getNumOutputIStream() + sm.getNumOutputIStream());
    this.aggSM.setNumOutputRStream(this.aggSM.getNumOutputRStream() + sm.getNumOutputRStream());
  }

  private synchronized void updateAggSM(StatementMetric sm) {
    aggSM.setTimestamp(sm.getTimestamp());
    aggSM.setCpuTime(sm.getCpuTime());
    aggSM.setWallTime(sm.getWallTime());
    aggSM.setNumInput(sm.getNumInput());
    aggSM.setNumOutputIStream(sm.getNumOutputIStream());
    aggSM.setNumOutputRStream(sm.getNumOutputRStream());
  }
}
