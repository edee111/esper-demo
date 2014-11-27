package cz.muni.fi;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Deprecated
public interface HelloMBean {
  public void setMessage(String message);
  public String getMessage();
  public void sayHello();
}
