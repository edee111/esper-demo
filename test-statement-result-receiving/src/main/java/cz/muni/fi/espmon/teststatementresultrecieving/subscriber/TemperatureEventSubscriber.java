package cz.muni.fi.espmon.teststatementresultrecieving.subscriber;

import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 23.5.15
 */
public class TemperatureEventSubscriber extends BaseSubscriber {

  private static final String[] STATEMENT_RESULT_NAMES = {"temperature", "serverName"};

  @Override
  public String getStatement() {
    return "select temperature, serverName from TemperatureEvent";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getStatementResultNames() {
    return STATEMENT_RESULT_NAMES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Map<String, Object> eventMap) {

    Integer temp = (Integer) eventMap.get("temperature");
    String serverName = (String) eventMap.get("serverName");

    StringBuilder sb = new StringBuilder();
    sb.append("\n---------------------------------");
    sb.append("\n- Server " + serverName + " Temp = " + temp);
    sb.append("\n---------------------------------");

    log.debug(sb.toString());
  }
}
