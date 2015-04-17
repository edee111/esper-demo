package cz.muni.fi.subscriber;

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
