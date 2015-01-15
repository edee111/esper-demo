package cz.muni.fi.handler;

import com.espertech.esper.client.EPServiceProvider;
import cz.muni.fi.event.AbstractEvent;
import cz.muni.fi.event.MemoryUsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eduard Tomek
 * @since 17.12.14
 */
public abstract class AbstractHandler {
  protected EPServiceProvider epService;
  protected Logger log = LoggerFactory.getLogger(getClass());

  public void handle(AbstractEvent event) {
    epService.getEPRuntime().sendEvent(event);
  }

}
