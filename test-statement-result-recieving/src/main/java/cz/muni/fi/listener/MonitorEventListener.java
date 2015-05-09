package cz.muni.fi.listener;

import cz.muni.fi.subscriber.MonitorEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class MonitorEventListener extends BaseListener {

  public MonitorEventListener() {
    this.baseSubscriber = new MonitorEventSubscriber();
  }
}
