package cz.muni.fi;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 13.11.14
 */
@Deprecated
public interface MemoryUsageMBean extends MBeanInf {
  public Date getTimestamp();
  public void setTimestamp(Date t);
  public long getUsage();
  public void setUsage(long l);
  public long getTotalMemory();
  public void setTotalMemory(long l);
}
