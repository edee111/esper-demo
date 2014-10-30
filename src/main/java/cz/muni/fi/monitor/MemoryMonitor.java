package cz.muni.fi.monitor;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;


/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class MemoryMonitor {
  /*
    ManagementFactory.getMemoryMXBean(); gives only this process memory information
    ManagementFactory.getMemoryPoolMXBeans(); gives pools of all processes in jvm
  */
  private OperatingSystemMXBean osMXBean;
  private long totalPhysicalMemorySize;

  public MemoryMonitor() {
    osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    totalPhysicalMemorySize = osMXBean.getTotalPhysicalMemorySize();
  }

  public synchronized long getTotalMemoryUsage() {
    long usedMemory = totalPhysicalMemorySize - osMXBean.getFreePhysicalMemorySize();
    return usedMemory;
  }

  public long getTotalMemorySize() {
    return totalPhysicalMemorySize;
  }

}
