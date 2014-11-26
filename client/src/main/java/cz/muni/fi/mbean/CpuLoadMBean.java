package cz.muni.fi.mbean;

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
  //-----------
  // operations
  //-----------

  public void sayHello();
  public int add(int x, int y);

  //-----------
  // attributes
  //-----------

  // a read-only attribute called Name of type String
  public String getName();

  // a read-write attribute called CacheSize of type int
  public int getCacheSize();
  public void setCacheSize(int size);
}
