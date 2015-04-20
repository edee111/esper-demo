package cz.muni.fi;

import com.espertech.esper.client.*;
import cz.muni.fi.jmx.SimpleAgent;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EsperMetricsMonitor {

  /**
   * Register monitoring using esper.cfg.xml file
   *
   * @throws EsperJMXException
   */
  public static void registerEsperMetricsMonitorFromFile(Configuration config) throws EsperJMXException {
    doRegister(config);
  }

  /**
   * Register monitoring from values
   *
   * @throws EsperJMXException
   */
  public static void registerEsperMetricsMonitorWithValues(Configuration config, long engineInterval, long statementInterval) throws EsperJMXException {
    ConfigurationMetricsReporting cmr = config.getEngineDefaults().getMetricsReporting();
    cmr.setEnableMetricsReporting(true);
    cmr.setEngineInterval(engineInterval);
    cmr.setStatementInterval(statementInterval);
    cmr.setThreading(true);
    doRegister(config);
  }

  private static void doRegister(Configuration config) throws EsperJMXException {
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

  public static void stop() throws EsperJMXException {
    SimpleAgent.getInstance().stop();
  }

}
