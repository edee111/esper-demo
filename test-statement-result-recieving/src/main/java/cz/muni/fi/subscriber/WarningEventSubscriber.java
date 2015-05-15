package cz.muni.fi.subscriber;


import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public class WarningEventSubscriber extends BaseSubscriber {


  /**
   * If 2 consecutive temperature events are greater than this
   * and the second is greater than the first - issue a warning
   */
  private static final String WARNING_EVENT_THRESHOLD = "60";

  private static final String[] STATEMENT_RESULT_NAMES = {"temp1", "temp2"};

  @Override
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

  @Override
  public String[] getStatementResultNames() {
    return STATEMENT_RESULT_NAMES;
  }

  @Override
  public void update(Map<String, Object> eventMap) {
    Object temp1 = eventMap.get("temp1");
    Object temp2 = eventMap.get("temp2");

    Object[] temps = {temp1, temp2};

    StringBuilder sb = new StringBuilder();
    sb.append("\n--------------------------------------------------");
    sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = " + temps[0] + "," + temps[1]);
    sb.append("\n--------------------------------------------------");

    log.debug(sb.toString());
  }
}
