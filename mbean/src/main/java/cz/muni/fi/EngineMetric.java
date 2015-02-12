package cz.muni.fi;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class EngineMetric extends MBean<EngineMetric> implements EngineMetricMBean {
  private long timestamp;
  private long inputCount;
  private long inputCountDelta;
  private long scheduleDepth;

  @Override
  public void update(EngineMetric mBean) {
    updateFieldsInternal(mBean);
    Notification n =
        new AttributeChangeNotification(
            this,
            sequenceNumber++,
            System.currentTimeMillis(),
            "EngineMetric changed",
            "EngineMetric",
            "EngineMetric",
            this,
            mBean
        );

    sendNotification(n);
  }

  private void updateFieldsInternal(EngineMetric mBean) {
    setTimestamp(mBean.getTimestamp());
    setInputCount(mBean.getInputCount());
    setInputCountDelta(mBean.getInputCountDelta());
    setScheduleDepth(mBean.getScheduleDepth());
  }

  @Override
  public String getLogInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("EngineMetric timestamp ");
    sb.append(sdf.format(timestamp));
    sb.append(" inputCount ");
    sb.append(inputCount);
    sb.append(" inputCountDelta ");
    sb.append(inputCountDelta);
    sb.append(" scheduleDepth ");
    sb.append(scheduleDepth);
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
  public long getInputCount() {
    return inputCount;
  }

  @Override
  public void setInputCount(long inputCount) {
    this.inputCount = inputCount;
  }

  @Override
  public long getInputCountDelta() {
    return inputCountDelta;
  }

  @Override
  public void setInputCountDelta(long inputCountDelta) {
    this.inputCountDelta = inputCountDelta;
  }

  @Override
  public long getScheduleDepth() {
    return scheduleDepth;
  }

  @Override
  public void setScheduleDepth(long scheduleDepth) {
    this.scheduleDepth = scheduleDepth;
  }
}
