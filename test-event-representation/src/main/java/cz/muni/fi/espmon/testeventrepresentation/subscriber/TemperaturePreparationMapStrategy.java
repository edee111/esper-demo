package cz.muni.fi.espmon.testeventrepresentation.subscriber;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class TemperaturePreparationMapStrategy implements TemperaturePreparationStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] prepareData(Object[] temps) {
    return temps;
  }
}