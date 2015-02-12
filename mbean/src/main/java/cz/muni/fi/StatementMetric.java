package cz.muni.fi;

import javax.management.Notification;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class StatementMetric extends MBean<StatementMetric> implements StatementMetricMBean {
  private long timestamp;
  private String statementName;
  private long cpuTime;
  private long wallTime;
  private long numInput;
  private long numOutputRStream;
  private long numOutputIStream;

  @Override
  public void update(StatementMetric mBean) {
    updateFieldsInternal(mBean);

    Notification n = new Notification("statementMetricMBean", this, sequenceNumber++); //todo use attributenotification if not working
    sendNotification(n);
  }

  private void updateFieldsInternal(StatementMetric mBean) {
    setTimestamp(mBean.getTimestamp());
    setStatementName(mBean.getStatementName());
    setCpuTime(mBean.getCpuTime());
    setWallTime(mBean.getWallTime());
    setNumInput(mBean.getNumInput());
    setNumOutputIStream(getNumOutputIStream());
    setNumOutputRStream(getNumOutputRStream());
  }

  @Override
  public String getLogInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("StatementMetric timestamp ");
    sb.append(sdf.format(timestamp));
    sb.append(" statementName ");
    sb.append(statementName);
    sb.append(" cpuTime ");
    sb.append(cpuTime);
    sb.append(" wallTime ");
    sb.append(wallTime);
    sb.append(" numInput ");
    sb.append(numInput);
    sb.append(" numOutputRStream ");
    sb.append(numOutputRStream);
    sb.append(" numOutputIStream ");
    sb.append(numOutputIStream);
    return sb.toString();
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String getStatementName() {
    return statementName;
  }

  @Override
  public void setStatementName(String statementName) {
    this.statementName = statementName;
  }

  @Override
  public long getCpuTime() {
    return cpuTime;
  }

  @Override
  public void setCpuTime(long cpuTime) {
    this.cpuTime = cpuTime;
  }

  @Override
  public long getWallTime() {
    return wallTime;
  }

  @Override
  public void setWallTime(long wallTime) {
    this.wallTime = wallTime;
  }

  @Override
  public long getNumInput() {
    return numInput;
  }

  @Override
  public void setNumInput(long numInput) {
    this.numInput = numInput;
  }

  @Override
  public long getNumOutputRStream() {
    return numOutputRStream;
  }

  @Override
  public void setNumOutputRStream(long numOutputRStream) {
    this.numOutputRStream = numOutputRStream;
  }

  @Override
  public long getNumOutputIStream() {
    return numOutputIStream;
  }

  @Override
  public void setNumOutputIStream(long numOutputIStream) {
    this.numOutputIStream = numOutputIStream;
  }
}
