package cz.muni.fi.subscriber;




import java.util.Arrays;
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
   * Listener method called when Esper has detected a pattern match.
   * Argument map values must be of type Object, because this type depends on event representation. If type
   * of map is wrong then update method does not match and is not called by Esper.
   */
  public void update(Map<String, Object> eventMap) {

    // 1st Temperature in the Warning Sequence
    Object temp1 = eventMap.get("temp1");
    // 2nd Temperature in the Warning Sequence
    Object temp2 = eventMap.get("temp2");


    //when representation of events is array transform result for logging to get values instead of pointer to array
    if (temp1 instanceof Object[] && temp2 instanceof Object[]) {
      Object[] temp1Arr = (Object[]) temp1;
      Object[] temp2Arr = (Object[]) temp2;
      temp1 = Arrays.asList(temp1Arr);
      temp2 = Arrays.asList(temp2Arr);
    }

    StringBuilder sb = new StringBuilder();
    sb.append("\n--------------------------------------------------");
    sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = " + temp1 + "," + temp2);
    sb.append("\n--------------------------------------------------");

    log.debug(sb.toString());
  }
}
