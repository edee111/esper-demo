package cz.muni.fi.event;

import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class TemperatureEvent {

  private int temperature;
  private Date timeOfReading;

  /**
   * Temerature constructor.
   *
   * @param temperature   Temperature in Celsius
   * @param timeOfReading Time of Reading
   */
  public TemperatureEvent(int temperature, Date timeOfReading) {
    this.temperature = temperature;
    this.timeOfReading = timeOfReading;
  }

  public int getTemperature() {
    return temperature;
  }

  public Date getTimeOfReading() {
    return timeOfReading;
  }

  @Override
  public String toString() {
    return "TemperatureEvent [" + temperature + "C]";
  }

}
