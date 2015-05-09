package cz.muni.fi.listener;

import cz.muni.fi.subscriber.WarningEventSubscriber;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public class WarningEventListener extends BaseListener {

  public WarningEventListener() {
    this.baseSubscriber = new WarningEventSubscriber();
  }
}
