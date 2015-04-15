package cz.muni.fi;

/**
 * @author Eduard Tomek
 * @since 12.2.15
 */
public class EsperJMXException extends Exception {
  public EsperJMXException() {
    super();
  }

  public EsperJMXException(String message) {
    super(message);
  }

  public EsperJMXException(String message, Throwable cause) {
    super(message, cause);
  }

  public EsperJMXException(Throwable cause) {
    super(cause);
  }

  protected EsperJMXException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
