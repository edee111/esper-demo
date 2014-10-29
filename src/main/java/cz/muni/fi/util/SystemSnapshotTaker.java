package cz.muni.fi.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.muni.fi.PerformanceMonitor;
import cz.muni.fi.event.CpuLoadEvent;
import cz.muni.fi.handler.CpuLoadEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Just a simple class to create a number of Random TemperatureEvents and pass them off to the
 * TemperatureEventHandler.
 */
@Component
public class SystemSnapshotTaker {

  /**
   * Logger
   */
  private static final Logger log = LoggerFactory.getLogger(SystemSnapshotTaker.class);

  @Autowired
  private CpuLoadEventHandler cpuLoadEventHandler;

  public void startTakingSystemSnapshots(final long noOfSnapthots) {

    ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

    xrayExecutor.submit(new Runnable() {
      public void run() {
        log.debug(getStartingMessage());

        int count = 0;
        PerformanceMonitor performanceMonitor = new PerformanceMonitor();
        while (count < noOfSnapthots) {
          CpuLoadEvent cle = new CpuLoadEvent(performanceMonitor.getCpuUsage(), new Date());
          cpuLoadEventHandler.handle(cle);

          count++;
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
            log.error("Thread Interrupted", e);
          }
        }

      }
    });
  }


  private String getStartingMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n************************************************************");
    sb.append("\n* STARTING *");
    sb.append("\n************************************************************\n");
    return sb.toString();
  }
}
