package cz.muni.fi.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.muni.fi.event.MemoryUsageEvent;
import cz.muni.fi.handler.MemoryUsageEventHandler;
import cz.muni.fi.monitor.CpuMonitor;
import cz.muni.fi.event.CpuLoadEvent;
import cz.muni.fi.handler.CpuLoadEventHandler;
import cz.muni.fi.monitor.MemoryMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemSnapshotTaker {

  /**
   * Logger
   */
  private static final Logger log = LoggerFactory.getLogger(SystemSnapshotTaker.class);

  @Autowired
  private CpuLoadEventHandler cpuLoadEventHandler;
  @Autowired
  private MemoryUsageEventHandler memoryUsageEventHandler;

  public void startTakingSystemSnapshots() {

    ExecutorService xrayExecutor = Executors.newFixedThreadPool(2);

    xrayExecutor.submit(new Runnable() {
      public void run() {
        log.debug(getStartingMessage());

        CpuMonitor cpuMonitor = new CpuMonitor();
        MemoryMonitor memMonitor = new MemoryMonitor();
        while (true) {
          Date now = new Date();
          CpuLoadEvent cle = new CpuLoadEvent(cpuMonitor.getSystemCpuUsage(), now);
          cpuLoadEventHandler.handle(cle);

          MemoryUsageEvent memoryUsageEvent =
                  new MemoryUsageEvent(memMonitor.getTotalMemorySize(), memMonitor.getTotalMemoryUsage(), now);
          memoryUsageEventHandler.handle(memoryUsageEvent);

          try {
            Thread.sleep(1000);
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
