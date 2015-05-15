package cz.muni.fi.espmon.testeventrepresentation.subscriber;

/**
 * @author Eduard Tomek
 * @since 27.2.15
 */
public interface StatementSubscriber {

  /**
   * Get the EPL Stamement the Subscriber will listen to.
   *
   * @return EPL Statement
   */
  public String getStatement();

}
