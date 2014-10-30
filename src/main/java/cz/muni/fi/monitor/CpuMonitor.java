package cz.muni.fi.monitor;


import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * @author Eduard Tomek
 * @since ${date}
 */
public class CpuMonitor {
  private long lastSystemTime = 0;
  private long lastProcessCpuTime = 0;

  private OperatingSystemMXBean osMXBean;
  private int availableProcessors;

  public CpuMonitor() {
    osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    availableProcessors = osMXBean.getAvailableProcessors();
  }

  public synchronized double getMyCpuUsage() {
    if (lastSystemTime == 0) {
      baselineCounters();
      return 0;
    }

    long systemTime = System.nanoTime();
    long processCpuTime = 0;

    processCpuTime = osMXBean.getProcessCpuTime();

    double cpuUsage = (double) (processCpuTime - lastProcessCpuTime) / (systemTime - lastSystemTime);

    lastSystemTime = systemTime;
    lastProcessCpuTime = processCpuTime;

    return cpuUsage / availableProcessors;
  }

  private void baselineCounters() {
    lastSystemTime = System.nanoTime();
    lastProcessCpuTime = osMXBean.getProcessCpuTime();
  }

  public synchronized double getSystemCpuUsage() {
    return osMXBean.getSystemCpuLoad();
  }
}
