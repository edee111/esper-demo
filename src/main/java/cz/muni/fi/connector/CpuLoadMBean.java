package cz.muni.fi.connector;

import javax.management.DynamicMBean;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public interface CpuLoadMBean {
  public Date getTimestamp();
  public void setTimestamp(Date t);
  public double getLoad();
  public void setLoad(double l);
}
