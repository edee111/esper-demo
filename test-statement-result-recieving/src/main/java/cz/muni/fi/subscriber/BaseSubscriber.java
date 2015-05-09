package cz.muni.fi.subscriber;

import com.espertech.esper.client.EventBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public abstract class BaseSubscriber {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  public abstract void update(Map<String, Object> eventMap);
}
