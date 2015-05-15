package cz.muni.fi.espmon.teststatementresultrecieving.listener;

import cz.muni.fi.espmon.teststatementresultrecieving.subscriber.CriticalEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class CriticalEventListener extends BaseListener {

  public CriticalEventListener() {
    this.baseSubscriber = new CriticalEventSubscriber();
  }
}
