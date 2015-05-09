package cz.muni.fi.event;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureEvent {
  // note: array represented events rely on fields order
  private Integer temperature;
  private Date timeOfReading;
  private String serverName;

  /**
   * Temerature constructor.
   *
   * @param temperature   Temperature in Celsius
   * @param timeOfReading Time of Reading
   */
  public TemperatureEvent(Integer temperature, Date timeOfReading, String serverName) {
    this.temperature = temperature;
    this.timeOfReading = timeOfReading;
    this.serverName = serverName;
  }

  public Integer getTemperature() {
    return temperature;
  }

  public Date getTimeOfReading() {
    return timeOfReading;
  }

  public String getServerName() {
    return serverName;
  }

  @Override
  public String toString() {
    return "TemperatureEvent{" +
        "temperature=" + temperature +
        ", timeOfReading=" + timeOfReading +
        ", serverName='" + serverName + '\'' +
        '}';
  }
}
