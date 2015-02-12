package cz.muni.fi.jmx;



import cz.muni.fi.MBean;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class SimpleAgent {
  private MBeanServer mbs = null;
  private Map<String, MBean> registeredMBeans = new HashMap<>();

  private static SimpleAgent instance;

  public static SimpleAgent getInstance() {
    if (instance == null) {
      instance = new SimpleAgent();
    }
    return instance;
  }

  private SimpleAgent() {
    mbs = ManagementFactory.getPlatformMBeanServer();
  }

  public void update(MBean mBean, Class eventClass) {

  }

  public void register(MBean mBean, Class eventClass) {
    try {
      ObjectName name = getObjectName(eventClass);
      if (mbs.isRegistered(name)) {
        MBean registeredMBean = registeredMBeans.get(mBean.getClass().getName());
        registeredMBean.update(mBean);
      }
      else {
        mbs.registerMBean(mBean, name);
        registeredMBeans.put(mBean.getClass().getName(), mBean);
      }
    } catch (MalformedObjectNameException e) {
      e.printStackTrace();
    } catch (NotCompliantMBeanException e) {
      e.printStackTrace();
    } catch (InstanceAlreadyExistsException e) {
      e.printStackTrace();
    } catch (MBeanRegistrationException e) {
      e.printStackTrace();
    }
  }

  private ObjectName getObjectName(Class clazz) throws MalformedObjectNameException {
    return new ObjectName(clazz.getPackage().getName() + ":type=" + clazz.getSimpleName());
  }
}