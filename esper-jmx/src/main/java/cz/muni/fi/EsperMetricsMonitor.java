package cz.muni.fi;

import com.espertech.esper.client.*;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EsperMetricsMonitor {

  /**
   * Register monitoring from code
   *
   * @throws EsperJMXException
   */
  public static void registerEsperMetricsMonitorFromFile() throws EsperJMXException {
    Configuration config = new Configuration();
    config.configure(); //use esper.cfg.xml file
    doRegister(config);
  }

  /**
   * Register monitoring from values
   *
   * @throws EsperJMXException
   */
  public static void registerEsperMetricsMonitorWithValues(long engineInterval, long statementInterval) throws EsperJMXException {
    Configuration config = new Configuration();
    ConfigurationMetricsReporting cmr = config.getEngineDefaults().getMetricsReporting();
    cmr.setEnableMetricsReporting(true);
    cmr.setEngineInterval(engineInterval);
    cmr.setStatementInterval(statementInterval);
    cmr.setThreading(true);
    doRegister(config);
  }

  private static void doRegister(Configuration config) throws EsperJMXException {
    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

    EPStatement epl2 = epService.getEPAdministrator().createEPL(getStatementMetricStatement(), "StatementMetricsStatement");
    epl2.addListener(StatementMetricListener.getInstance());

    EPStatement epl = epService.getEPAdministrator().createEPL(getEngineMetricStatement(), "EngineMetricsStatement");
    epl.addListener(EngineMetricListener.getInstance());
  }

  private static String getEngineMetricStatement() {
    return "select * from com.espertech.esper.client.metric.EngineMetric";
  }

  private static String getStatementMetricStatement() {
    return "select * from com.espertech.esper.client.metric.StatementMetric";
  }

}
