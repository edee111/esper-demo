package cz.muni.fi.subscriber;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public interface TemperaturePreparationStrategy {

  public Object[] prepareData(Object[] temps);
}
