package cz.muni.fi.connector;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Deprecated
public class Hello implements HelloMBean {
  private String message = null;

  public Hello() {
    message = "Hello, world";
  }

  public Hello(String message) {
    this.message = message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void sayHello() {
    System.out.println(message);
  }
}