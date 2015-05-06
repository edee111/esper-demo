package cz.muni.fi;

/**
 * Thrown to indicate that unexpected state of espmon-client application occured
 *
 * @author Eduard Tomek
 * @since 6.2.15
 */
public class EspmonClientException extends Exception {

  public EspmonClientException(String message) {
    super(message);
  }

  public EspmonClientException(String message, Throwable cause) {
    super(message, cause);
  }
}