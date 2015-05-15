package cz.muni.fi.espmon.teststatementresultrecieving.subscriber;



import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class CriticalEventSubscriber extends BaseSubscriber {
  private static final String[] STATEMENT_RESULT_NAMES = {"temp1", "temp2", "temp3", "temp4"};

  /**
   * Used as the minimum starting threshold for a critical event.
   */
  private static final String CRITICAL_EVENT_THRESHOLD = "65";

  /**
   * If the last event in a critical sequence is this much greater than the first - issue a
   * critical alert.
   */
  private static final String CRITICAL_EVENT_MULTIPLIER = "1.15";

  @Override
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

  @Override
  public String[] getStatementResultNames() {
    return STATEMENT_RESULT_NAMES;
  }

  @Override
  public void update(Map<String, Object> eventMap) {
    Object temp1 = eventMap.get("temp1");
    Object temp2 = eventMap.get("temp2");
    Object temp3 = eventMap.get("temp3");
    Object temp4 = eventMap.get("temp4");

    Object[] temps = {temp1, temp2, temp3, temp4};

    StringBuilder sb = new StringBuilder();
    sb.append("\n***************************************");
    sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
    sb.append("\n* " + temps[0] + " > " + temps[1] + " > " + temps[2] + " > " + temps[3]);
    sb.append("\n***************************************");

    this.log.debug(sb.toString());
  }

}
