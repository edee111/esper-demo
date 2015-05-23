package cz.muni.fi.espmon.example.event;

import java.util.Date;

/**
 * Class representing one taking of temperature on server
 *
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

  @Override
  public String toString() {
    return "TemperatureEvent{" +
        "temperature=" + temperature +
        ", timeOfReading=" + timeOfReading +
        ", serverName='" + serverName + '\'' +
        '}';
  }
}
