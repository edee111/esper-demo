package cz.muni.fi.espmonjmx;

import com.espertech.esper.client.*;
import cz.muni.fi.espmonjmx.jmx.JMXAgent;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EsperMetricsMonitor {

  public static void enableEsperMetricsMonitoring(Configuration config, long engineInterval, long statementInterval) throws EspmonJMXException {
    enableEsperMetricsMonitoring(config, engineInterval, statementInterval, null);
  }

  /**
   * Enable metric monitoring
   *
   * @throws EspmonJMXException
   */
  public static void enableEsperMetricsMonitoring(Configuration config, long engineInterval, long statementInterval, Integer port) throws EspmonJMXException {
    ConfigurationMetricsReporting cmr = config.getEngineDefaults().getMetricsReporting();
    cmr.setEnableMetricsReporting(true);
    cmr.setEngineInterval(engineInterval);
    cmr.setStatementInterval(statementInterval);
    cmr.setThreading(true);
    if (port != null) {
      JMXAgent.setMBeanServerPort(port);
    }
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
    smEpl.setSubscriber(new StatementMetricSubscriber());

    EPStatement emEpl = epService.getEPAdministrator().createEPL(getEngineMetricStatement(), "EngineMetricsStatement");
    emEpl.setSubscriber(new EngineMetricSubscriber());
  }

  private static String getEngineMetricStatement() {
    return "select * from com.espertech.esper.client.metric.EngineMetric";
  }

  private static String getStatementMetricStatement() {
    return "select * from com.espertech.esper.client.metric.StatementMetric";
  }

  public static void stopEsperMetricsMonitoring() throws EspmonJMXException {
    JMXAgent.getInstance().stop();
  }

}
