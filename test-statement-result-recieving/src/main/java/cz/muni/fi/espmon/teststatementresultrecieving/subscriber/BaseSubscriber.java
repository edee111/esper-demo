package cz.muni.fi.espmon.teststatementresultrecieving.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public abstract class BaseSubscriber implements StatementSubscriber {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  /**
   * Subscriber method called when Esper has detected a pattern match.
   */
  public abstract void update(Map<String, Object> eventMap);
}
