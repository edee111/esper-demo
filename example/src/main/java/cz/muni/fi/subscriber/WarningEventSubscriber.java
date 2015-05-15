package cz.muni.fi.subscriber;



import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class WarningEventSubscriber extends AbstractSubscriber implements StatementSubscriber {

  /**
   * If 2 consecutive temperature events are greater than this
   * and the second is greater than the first - issue a warning
   */
  private static final String WARNING_EVENT_THRESHOLD = "60";

  /**
   * {@inheritDoc}
   */
  public String getStatement() {

    // Example using 'Match Recognise' syntax.
    String warningEventExpression = "select * from TemperatureEvent "
            + "match_recognize ( "
            + "       partition by serverName "
            + "       measures A as temp1, B as temp2 "
            + "       pattern (A B) "
            + "       define "
            + "               A as A.temperature > " + WARNING_EVENT_THRESHOLD + ", "
            + "               B as B.temperature > " + WARNING_EVENT_THRESHOLD + " and B.temperature > A.temperature)";

    return warningEventExpression;
  }

  /**
   * Subscriber method called when Esper has detected a pattern match.
   * Argument map values must be of type Object, because this type depends on event representation. If type
   * of map is wrong then update method does not match and is not called by Esper.
   */
  public void update(Map<String, Object> eventMap) {
    Object temp1 = eventMap.get("temp1");
    Object temp2 = eventMap.get("temp2");

    StringBuilder sb = new StringBuilder();
    sb.append("\n--------------------------------------------------");
    sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = " + temp1 + "," + temp2);
    sb.append("\n--------------------------------------------------");

    log.debug(sb.toString());
  }
}
