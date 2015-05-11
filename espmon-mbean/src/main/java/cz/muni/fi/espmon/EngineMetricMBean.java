package cz.muni.fi.espmon;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public interface EngineMetricMBean extends MBeanInf {

  public String getEngineURI();
  public void setEngineURI(String engineURI);
  public long getTimestamp();
  public void setTimestamp(long timestamp);
  public long getInputCount();
  public void setInputCount(long inputCount);
  public long getInputCountDelta();
  public void setInputCountDelta(long inputCountDelta);
  public long getScheduleDepth();
  public void setScheduleDepth(long scheduleDepth);

}
