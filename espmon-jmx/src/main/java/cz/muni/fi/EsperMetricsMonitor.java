package cz.muni.fi;

import com.espertech.esper.client.*;
import cz.muni.fi.jmx.JMXAgent;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EsperMetricsMonitor {

  /**
   * Enable metric monitoring
   *
   * @throws EspmonJMXException
   */
  public static void enableEsperMetricsMonitoring(Configuration config, long engineInterval, long statementInterval) throws EspmonJMXException {
    ConfigurationMetricsReporting cmr = config.getEngineDefaults().getMetricsReporting();
    cmr.setEnableMetricsReporting(true);
    cmr.setEngineInterval(engineInterval);
    cmr.setStatementInterval(statementInterval);
    cmr.setThreading(true);
    doEnable(config);
  }

  /**
   * Create engine and statement metrics EPL's and set them subscribers
   *
   * @param config
   * @throws EspmonJMXException
   */
  private static void doEnable(Configuration config) throws EspmonJMXException {
    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

    EPStatement smEpl = epService.getEPAdministrator().createEPL(getStatementMetricStatement(), "StatementMetricsStatement");
    smEpl.setSubscriber(StatementMetricSubscriber.getInstance());

    EPStatement emEpl = epService.getEPAdministrator().createEPL(getEngineMetricStatement(), "EngineMetricsStatement");
    emEpl.setSubscriber(EngineMetricSubscriber.getInstance());
  }

  private static String getEngineMetricStatement() {
    return "select * from com.espertech.esper.client.metric.EngineMetric";
  }

  private static String getStatementMetricStatement() {
    return "select * from com.espertech.esper.client.metric.StatementMetric";
  }

  public static void stop() throws EspmonJMXException {
    JMXAgent.getInstance().stop();
  }

}
