package cz.muni.fi.espmon.teststatementresultrecieving.listener;

import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.WarningEventSubscriber;

/**
 * Listener for receiving warning events
 *
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class WarningEventListener extends BaseListener {

  public WarningEventListener() {
    this.baseSubscriber = new WarningEventSubscriber();
  }
}
