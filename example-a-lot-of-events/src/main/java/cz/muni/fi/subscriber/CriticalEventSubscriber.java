package cz.muni.fi.subscriber;



import cz.muni.fi.event.TemperatureEvent;

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
  public void update(Map<String, TemperatureEvent> eventMap) {

    // 1st Temperature in the Critical Sequence
    TemperatureEvent temp1 = (TemperatureEvent) eventMap.get("temp1");
    // 2nd Temperature in the Critical Sequence
    TemperatureEvent temp2 = (TemperatureEvent) eventMap.get("temp2");
    // 3rd Temperature in the Critical Sequence
    TemperatureEvent temp3 = (TemperatureEvent) eventMap.get("temp3");
    // 4th Temperature in the Critical Sequence
    TemperatureEvent temp4 = (TemperatureEvent) eventMap.get("temp4");

    StringBuilder sb = new StringBuilder();
    sb.append("\n***************************************");
    sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
    sb.append("\n* " + temp1 + " > " + temp2 + " > " + temp3 + " > " + temp4);
    sb.append("\n***************************************");

    this.log.debug(sb.toString());
  }


}
