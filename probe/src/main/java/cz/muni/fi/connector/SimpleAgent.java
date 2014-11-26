package cz.muni.fi.connector;


import org.springframework.stereotype.Component;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * @author Eduard Tomek
 * @since 30.10.14
 */
@Component
public class SimpleAgent {
  private MBeanServer mbs = null;

  public SimpleAgent() {

    // Get the platform MBeanServer
    mbs = ManagementFactory.getPlatformMBeanServer();

    // Unique identification of MBeans
    Hello helloBean = new Hello();
    ObjectName helloName = null;

    try {
      // Uniquely identify the MBeans and register them with the platform MBeanServer
      helloName = new ObjectName("FOO:name=HelloBean");
      mbs.registerMBean(helloBean, helloName);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void register(MBean mBean, Class eventClass) {
    try {
      ObjectName name = getObjectName(eventClass);
      System.out.println(name);
      if (mbs.isRegistered(name)) { //todo inform other side about attribute change
        AttributeList attList = getMBeanAttributes(mBean);
        mbs.setAttributes(name, attList);
      }
      else {
        mbs.registerMBean(mBean, name);
      }
    } catch (MalformedObjectNameException e) {
      e.printStackTrace();
    } catch (NotCompliantMBeanException e) {
      e.printStackTrace();
    } catch (InstanceAlreadyExistsException e) {
      e.printStackTrace();
    } catch (MBeanRegistrationException e) {
      e.printStackTrace();
    } catch (InstanceNotFoundException e) {
      e.printStackTrace();
    } catch (ReflectionException e) {
      e.printStackTrace();
    }
  }

  private ObjectName getObjectName(Class clazz) throws MalformedObjectNameException {
    return new ObjectName(clazz.getPackage().getName() + ":name=" + clazz.getSimpleName());
  }

  private AttributeList getMBeanAttributes(MBean mBean) {
    AttributeList attList = new AttributeList();
    if (mBean instanceof CpuLoad) {
      CpuLoad cpuLoadMbean = (CpuLoad) mBean;
      attList.add(new Attribute("Timestamp", cpuLoadMbean.getTimestamp()));
      attList.add(new Attribute("Load", cpuLoadMbean.getLoad()));
    }
    else if (mBean instanceof MemoryUsage) {
      MemoryUsage memoryUsageMBean = (MemoryUsage) mBean;
      attList.add(new Attribute("Timestamp", memoryUsageMBean.getTimestamp()));
      attList.add(new Attribute("Load", memoryUsageMBean.getUsage()));
    }
    return attList;
  }
}