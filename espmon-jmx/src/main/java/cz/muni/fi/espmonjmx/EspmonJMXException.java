package cz.muni.fi.espmonjmx;

/**
 * Thrown to indicate that unexpected state of espmon-jmx application occured
 *
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class EspmonJMXException extends Exception {

  public EspmonJMXException() {
    super();
  }

  public EspmonJMXException(String message) {
    super(message);
  }

  public EspmonJMXException(String message, Throwable cause) {
    super(message, cause);
  }

  public EspmonJMXException(Throwable cause) {
    super(cause);
  }

  protected EspmonJMXException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
