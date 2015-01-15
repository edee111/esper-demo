package cz.muni.fi.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cz.muni.fi.runtime.ClientListener;

/**
 * @author Eduard Tomek
 * @since 15.1.15
 */
public class OmitEventsInProductionFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (event.getLoggerName().equals(ClientListener.class.getName())) {
      return FilterReply.DENY;
    } else {
      return FilterReply.ACCEPT;
    }
  }
}