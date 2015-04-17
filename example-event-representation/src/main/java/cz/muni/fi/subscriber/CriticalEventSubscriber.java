package cz.muni.fi.subscriber;



import cz.muni.fi.event.TemperatureEvent;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class CriticalEventSubscriber extends AbstractSubscriber implements StatementSubscriber {
  /**
   * Used as the minimum starting threshold for a critical event.
   */
  private static final String CRITICAL_EVENT_THRESHOLD = "65";

  /**
   * If the last event in a critical sequence is this much greater than the first - issue a
   * critical alert.
   */
  private static final String CRITICAL_EVENT_MULTIPLIER = "1.15";

  /**
   * {@inheritDoc}
   */
  public String getStatement() {

    // Example using 'Match Recognise' syntax.
    String criticalEventExpression = "select * from TemperatureEvent "
            + "match_recognize ( "
            + "       partition by serverName "
            + "       measures A as temp1, B as temp2, C as temp3, D as temp4 "
            + "       pattern (A B C D) "
            + "       define "
            + "               A as A.temperature > " + CRITICAL_EVENT_THRESHOLD + ", "
            + "               B as (A.temperature < B.temperature), "
            + "               C as (B.temperature < C.temperature), "
            + "               D as (C.temperature < D.temperature) and D.temperature > (A.temperature * " + CRITICAL_EVENT_MULTIPLIER + ")" + ")";

    return criticalEventExpression;
  }

  /**
   * Listener method called when Esper has detected a pattern match.
   */
  public void update(Map<String, Object> eventMap) {

    // 1st Temperature in the Critical Sequence
    Object temp1 = eventMap.get("temp1");
    // 2nd Temperature in the Critical Sequence
    Object temp2 = eventMap.get("temp2");
    // 3rd Temperature in the Critical Sequence
    Object temp3 = eventMap.get("temp3");
    // 4th Temperature in the Critical Sequence
    Object temp4 = eventMap.get("temp4");

    //when representation of events is array transform result for logging to get values instead of pointer to array
    if (temp1 instanceof Object[] && temp2 instanceof Object[]) {
      Object[] temp1Arr = (Object[]) temp1;
      Object[] temp2Arr = (Object[]) temp2;
      Object[] temp3Arr = (Object[]) temp3;
      Object[] temp4Arr = (Object[]) temp4;
      temp1 = Arrays.asList(temp1Arr);
      temp2 = Arrays.asList(temp2Arr);
      temp3 = Arrays.asList(temp3Arr);
      temp4 = Arrays.asList(temp4Arr);
    }

    StringBuilder sb = new StringBuilder();
    sb.append("\n***************************************");
    sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
    sb.append("\n* " + temp1 + " > " + temp2 + " > " + temp3 + " > " + temp4);
    sb.append("\n***************************************");

    this.log.debug(sb.toString());
  }

}
