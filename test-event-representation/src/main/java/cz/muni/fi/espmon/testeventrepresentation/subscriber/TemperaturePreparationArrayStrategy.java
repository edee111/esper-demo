package cz.muni.fi.espmon.testeventrepresentation.subscriber;

import java.util.Arrays;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class TemperaturePreparationArrayStrategy implements TemperaturePreparationStrategy {
  @Override
  public Object[] prepareData(Object[] temps) {
    for (int i = 0; i < temps.length; i++) {
      Object[] tempArr = (Object[]) temps[i];
      temps[i] = Arrays.asList(tempArr);
    }
    return temps;
  }
}
