package cz.muni.fi;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 13.11.14
 */
public interface MemoryUsageMBean {
  public Date getTimestamp();
  public void setTimestamp(Date t);
  public long getUsage();
  public void setUsage(long l);
}
