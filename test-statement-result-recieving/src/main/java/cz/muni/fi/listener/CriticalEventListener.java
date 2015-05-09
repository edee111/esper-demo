package cz.muni.fi.listener;

import cz.muni.fi.subscriber.CriticalEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class CriticalEventListener extends AbstractListener {

  public CriticalEventListener() {
    this.baseSubscriber = new CriticalEventSubscriber();
  }
}
