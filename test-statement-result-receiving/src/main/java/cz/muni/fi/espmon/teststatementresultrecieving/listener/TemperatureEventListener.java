package cz.muni.fi.espmon.teststatementresultrecieving.listener;

import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.TemperatureEventSubscriber;
import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.WarningEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 23.5.15
 */
public class TemperatureEventListener extends BaseListener {
  public TemperatureEventListener() {
    this.baseSubscriber = new TemperatureEventSubscriber();
  }
}
