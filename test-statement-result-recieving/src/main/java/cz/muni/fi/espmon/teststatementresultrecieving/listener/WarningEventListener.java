package cz.muni.fi.espmon.teststatementresultrecieving.listener;

import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.WarningEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class WarningEventListener extends BaseListener {

  public WarningEventListener() {
    this.baseSubscriber = new WarningEventSubscriber();
  }
}
