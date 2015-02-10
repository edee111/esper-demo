package cz.muni.fi;

/**
 * @author Eduard Tomek
 * @since 6.2.15
 */
public class EspMonException extends Exception {
  public EspMonException(String message) {
    super(message);
  }

  public EspMonException(String message, Throwable cause) {
    super(message, cause);
  }
}