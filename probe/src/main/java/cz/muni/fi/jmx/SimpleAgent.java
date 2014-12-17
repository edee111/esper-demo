package cz.muni.fi.jmx;


import cz.muni.fi.CpuLoad;
import cz.muni.fi.MBean;
import cz.muni.fi.MemoryUsage;
import org.springframework.stereotype.Component;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Component
public class SimpleAgent {
  private MBeanServer mbs = null;
  private Map<String, MBean> registeredMBeans = new HashMap<>();

  public SimpleAgent() {
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