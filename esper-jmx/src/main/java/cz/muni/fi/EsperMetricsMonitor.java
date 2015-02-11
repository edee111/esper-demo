package cz.muni.fi;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * @author Eduard Tomek
 * @since 11.2.15
 */
public class EsperMetricsMonitor {

  private EPServiceProvider epService;

  //todo when esper server configured programatically - configure will not work, consider passing epService
  public EsperMetricsMonitor() {
    Configuration config = new Configuration();
    config.configure();
    this.epService = EPServiceProviderManager.getDefaultProvider(config);
    this.initMonitor();
  }

  private void initMonitor() {
    EPStatement epl2 = epService.getEPAdministrator().createEPL(getStatementMetricStatement());
    epl2.addListener(new StatementMetricsListener());

    EPStatement epl = epService.getEPAdministrator().createEPL(getEngineMetricStatement());
    epl.addListener(new EngineMetricListener());
  }

  private String getEngineMetricStatement() {
    return "select * from com.espertech.esper.client.metric.EngineMetric";
  }

  private String getStatementMetricStatement() {
    return "select * from com.espertech.esper.client.metric.StatementMetric";
  }

}
