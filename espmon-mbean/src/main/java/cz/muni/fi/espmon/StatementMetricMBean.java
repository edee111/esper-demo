package cz.muni.fi.espmon;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public interface StatementMetricMBean extends MBeanInf {

  public long getTimestamp();

  public void setTimestamp(long timestamp);

  public String getStatementName();

  public void setStatementName(String statementName);

  public long getCpuTime();

  public void setCpuTime(long cpuTime);

  public long getWallTime();

  public void setWallTime(long wallTime);

  public long getNumInput();

  public void setNumInput(long numInput);

  public long getNumOutputRStream();

  public void setNumOutputRStream(long numOutputRStream);

  public long getNumOutputIStream();

  public void setNumOutputIStream(long numOutputIStream);
}
