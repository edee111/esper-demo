package cz.muni.fi.runtime;

import cz.muni.fi.CpuLoadMBean;
import cz.muni.fi.MBeanInf;
import cz.muni.fi.MemoryUsageMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Eduard Tomek
 * @since 17.12.14
 */
@Component
public class JMXClient {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private Set<MBeanInf> beans = new HashSet();
  private JMXServiceURL url;
  private JMXConnector jmxc;
  private MBeanServerConnection mbsc;

  public void connect(String jmxServiceUrl) {
    try {
      log.debug("\nCreating an RMI connector client and " +
              "connect it to the RMI connector server " + jmxServiceUrl);
      url =
              new JMXServiceURL(jmxServiceUrl);
      jmxc = JMXConnectorFactory.connect(url, null);
      mbsc = jmxc.getMBeanServerConnection();
    } catch (IOException e) {
      log.error("Unable to connect", e);
    }
  }

  public void createMBeanProxies() {
    createMbeanProxy(CpuLoadMBean.class, "cz.muni.fi:type=CpuLoad");
    createMbeanProxy(MemoryUsageMBean.class, "cz.muni.fi:type=MemoryUsage");
  }

  public void createMbeanProxy(Class<? extends MBeanInf> mbeanClass, String objectName) {
    ObjectName mbeanName = null;
    try {
      mbeanName = new ObjectName(objectName);
    } catch (MalformedObjectNameException e) {
      log.error("Unable to create object name from " + objectName, e);
      return;
    }

    MBeanInf mBean = JMX.newMBeanProxy(mbsc, mbeanName, mbeanClass, true);

    ClientListener listener = new ClientListener();
    createMBeanNotificationListener(mbeanName, listener);
    beans.add(mBean);
  }


  public void createMBeanNotificationListener(ObjectName mBeanName, NotificationListener listener) {
    log.debug("Adding notification listener for JMXClient " + url.getURLPath());
    try {
      mbsc.addNotificationListener(mBeanName, listener, null, null);
    } catch (InstanceNotFoundException | IOException e) {
      log.error("Unable to add notification listener", e);
    }
    return;
  }

  @Deprecated //todo make it better
  public void listen() {
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        log.error("Exception while sleeping", e);
      }
    }
  }

  public void close() {
    try {
      jmxc.close();
    } catch (IOException e) {
      log.error("Unable to close JMX connector", e);
    }
    log.info("JMX connector successfully closed");
  }
}
