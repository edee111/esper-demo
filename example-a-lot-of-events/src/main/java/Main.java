import cz.muni.fi.EsperJMXException;
import cz.muni.fi.EsperMetricsMonitor;

import java.lang.management.ManagementFactory;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class Main {

  public static void main(String[] args) throws EsperJMXException {
    EsperMetricsMonitor.registerEsperMetricsMonitorWithValues(5000, 5000);

    //udelat neco ve stylu sberna teplot v serverovne - vice vlaken - kazde vlakno jako jeden server chrli teplotu systemu
    //ktere jsou eventy a bude jich hodne
  }
}
