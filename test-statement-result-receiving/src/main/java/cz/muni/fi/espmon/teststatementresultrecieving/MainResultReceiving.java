package cz.muni.fi.espmon.teststatementresultrecieving;

import com.espertech.esper.client.Configuration;
import cz.muni.fi.espmonjmx.EsperMetricsMonitor;
import cz.muni.fi.espmonjmx.EspmonJMXException;
import cz.muni.fi.espmon.teststatementresultrecieving.event.TemperatureEvent;
import cz.muni.fi.espmon.teststatementresultrecieving.handler.TemperatureEventHandler;
import cz.muni.fi.espmon.teststatementresultrecieving.monitor.TemperatureMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class MainResultReceiving {
  private static final int SERVER_COUNT = 50;
  private static final int DEFAULT_DURATION = 3600;
  private static final Logger log = LoggerFactory.getLogger(MainResultReceiving.class);

  private static final int ENGINE_INTERVAL = 5000;
  private static final int STATEMENT_INTERVAL = 5000;

  public static void main(String[] args) throws EspmonJMXException {
    int duration = DEFAULT_DURATION;
    ResultReceivingType recievingType;
    int port;

    if (args.length == 3) {
        duration = Integer.valueOf(args[0]);
        recievingType = ResultReceivingType.valueOfStr(args[1]);
        port = Integer.valueOf(args[2]);
    }
    else {
      log.error("Incorrect number of arguments. Arguments are: " +
              "durationInSeconds recievingType(SUBSCRIBER|LISTENER) portNumber");
      return;
    }

    runTest(duration, recievingType, port);
  }

  private static void runTest(int durationInSeconds, ResultReceivingType resultReceivingType, int port) throws EspmonJMXException {
    Configuration config = new Configuration();
    registerEventType(config);

    /*
     * Not all statements may report metrics: The engine performs certain runtime optimizations sharing resources
     * between similar statements, thereby not reporting on certain statements unless resource sharing
     * is disabled through configuration.
     * Esper refference 16.4.12.1.
     * We want real statement metrics results for purpose of this test. Optimization disabled.
     */
    config.getEngineDefaults().getViewResources().setShareViews(false);

    EsperMetricsMonitor.enableEsperMetricsMonitoring(config, ENGINE_INTERVAL, STATEMENT_INTERVAL, port);

    TemperatureEventHandler.init(config, resultReceivingType);
    runExecution(durationInSeconds);
  }

  private static void runExecution(int durationInSeconds) throws EspmonJMXException {
    ExecutorService exSvc = Executors.newFixedThreadPool(SERVER_COUNT);

    for (int i = 1; i <= SERVER_COUNT; i++) {
      exSvc.execute(new TemperatureMonitor(String.valueOf(i)));
    }

    try {
      Thread.sleep(durationInSeconds * 1000);
    } catch (InterruptedException e) {
      log.error("Interrupted exception while running test.", e);
      return;
    }

    stop(exSvc);
  }

  private static void registerEventType(Configuration config) throws EspmonJMXException {
    Field[] fields = TemperatureEvent.class.getDeclaredFields();
    String[] fieldNames = new String[fields.length];
    Object[] fieldTypes = new Object[fields.length];
    for (int i = 0; i < fields.length; i++ ) {
      Field f = fields[i];
      fieldNames[i] = f.getName();
      fieldTypes[i] = f.getType();
    }

    config.addEventType(TemperatureEvent.class.getSimpleName(), fieldNames, fieldTypes);
  }

  private static void stop(ExecutorService exSvc) throws EspmonJMXException {
    TemperatureMonitor.stopMonitoring();
    exSvc.shutdown();
    EsperMetricsMonitor.stopEsperMetricsMonitoring();
  }
}
