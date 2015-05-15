package cz.muni.fi.espmon.testeventrepresentation.subscriber;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class TemperaturePreparationPOJOStrategy implements TemperaturePreparationStrategy {
  @Override
  public Object[] prepareData(Object[] temps) {
    return temps;
  }
}
