package cz.muni.fi.espmonclient.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cz.muni.fi.espmonclient.jmx.ClientListener;

/**
 * Logback filter to get rid of event logs in production log
 *
 * @author Eduard Tomek
 * @since 15.1.15
 */
public class OmitEventsInProductionFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    boolean isEventLog =
            event.getLoggerName().equals(ClientListener.class.getName()) && Level.INFO.equals(event.getLevel());

    if (isEventLog) {
      return FilterReply.DENY;
    } else {
      return FilterReply.ACCEPT;
    }
  }
}