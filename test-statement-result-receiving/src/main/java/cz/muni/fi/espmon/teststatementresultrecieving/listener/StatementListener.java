package cz.muni.fi.espmon.teststatementresultrecieving.listener;

/**
 * Interface for listener supporting obtaining their statement
 *
 * @author Eduard Tomek
 * @since 9.5.15
 */
public interface StatementListener {

  /**
   * Get the EPL Stamement the listener will listen to.
   *
   * @return EPL Statement
   */
  public String getStatement();
}
