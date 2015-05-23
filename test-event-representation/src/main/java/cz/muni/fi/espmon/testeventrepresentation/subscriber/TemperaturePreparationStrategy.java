package cz.muni.fi.espmon.testeventrepresentation.subscriber;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public interface TemperaturePreparationStrategy {

  /**
   * Transform data from its representation to representation which is printable
   *
   * @param temps temperature event objects
   * @return printable representation of temps
   */
  public Object[] prepareData(Object[] temps);
}
