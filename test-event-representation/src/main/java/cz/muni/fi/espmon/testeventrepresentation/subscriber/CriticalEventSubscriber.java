package cz.muni.fi.espmon.testeventrepresentation.subscriber;



import cz.muni.fi.espmon.testeventrepresentation.EventRepresentation;

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

  private TemperaturePreparationStrategy tempPrepStrat;

  public CriticalEventSubscriber(EventRepresentation repr) {
    switch (repr) {
      case POJO: this.tempPrepStrat = new TemperaturePreparationPOJOStrategy();
      case MAP: this.tempPrepStrat = new TemperaturePreparationMapStrategy();
      case ARRAY: this.tempPrepStrat = new TemperaturePreparationArrayStrategy();
      case XML: this.tempPrepStrat = new TemperaturePreparationXMLStrategy();
    }
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * Subscriber method called when Esper has detected a pattern match.
   */
  public void update(Map<String, Object> eventMap) {
    Object temp1 = eventMap.get("temp1");
    Object temp2 = eventMap.get("temp2");
    Object temp3 = eventMap.get("temp3");
    Object temp4 = eventMap.get("temp4");

    Object[] temps = {temp1, temp2, temp3, temp4};
    temps = tempPrepStrat.prepareData(temps);

    StringBuilder sb = new StringBuilder();
    sb.append("\n***************************************");
    sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
    sb.append("\n* " + temps[0] + " > " + temps[1] + " > " + temps[2] + " > " + temps[3]);
    sb.append("\n***************************************");

    this.log.debug(sb.toString());
  }

}
