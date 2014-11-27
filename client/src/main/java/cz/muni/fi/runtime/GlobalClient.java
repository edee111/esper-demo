package cz.muni.fi.runtime;

import cz.muni.fi.CpuLoadMBean;
import cz.muni.fi.MBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Eduard Tomek
 * @since 6.11.14
 */
@Component
public class GlobalClient {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private Set<MBean> beans;

  private MBeanServerConnection mbsc;
  private JMXConnector jmxc;

  public void connect() {
    try {
      // Create an RMI connector client and
      // connect it to the RMI connector server
      //
      log.debug("\nCreate an RMI connector client and " +
              "connect it to the RMI connector server");
      JMXServiceURL url =
              new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
      jmxc = JMXConnectorFactory.connect(url, null);


      // Get an MBeanServerConnection
      //
      log.debug("Get an MBeanServerConnection");

      mbsc = jmxc.getMBeanServerConnection();
    } catch (IOException e) {
      log.error("Unable to connect", e);
    }
    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
  }

  public void getConnectionInfo() {
    try {
      // Get domains from MBeanServer
      //
      log.debug("Domains:");
      String domains[] = mbsc.getDomains();
      Arrays.sort(domains);
      for (String domain : domains) {
        log.debug("Domain = " + domain);
      }
      // Get MBeanServer's default domain
      //

      log.debug("MBeanServer default domain = " + mbsc.getDefaultDomain());


      // Get MBean count
      //
      log.debug("MBean count = " + mbsc.getMBeanCount());

      // Query MBean names
      //
      log.debug("Query MBeanServer MBeans:");
      Set<ObjectName> names =
              new TreeSet<ObjectName>(mbsc.queryNames(null, null));
      for (ObjectName name : names) {
        log.debug("ObjectName = " + name);
      }
    } catch (IOException e) {
      log.error("Unable to get connection info", e);
    }
  }

  public void createMBeanProxies() {
    createMbeanProxy(CpuLoadMBean.class, "cz.muni.fi:type=CpuLoad");
    //createMbeanProxy((Class<? extends MBean>) CpuLoadMBean.class, "cz.muni.fi:type=CpuLoad");
  }

  public void createMbeanProxy(Class<CpuLoadMBean> mbeanClass, String objectName) {
    ObjectName mbeanName = null;
    try {
      mbeanName = new ObjectName(objectName);
    } catch (MalformedObjectNameException e) {
      log.error("Unable to create object name from " + objectName, e);
      return;
    }

    MBean mBean = (MBean) JMX.newMBeanProxy(mbsc, mbeanName, mbeanClass, true);

    ClientListener listener = new ClientListener();
    createMBeanNotificationListener(mbeanName, listener);
    beans.add(mBean);
  }


  public void createMBeanNotificationListener(ObjectName mBeanName, NotificationListener listener) {
    log.debug("Add notification listener...");
    try {
      mbsc.addNotificationListener(mBeanName, listener, null, null);
    } catch (InstanceNotFoundException | IOException e) {
      log.error("Unable to add notification listener", e);
    }
    return;
  }
  /* For simplicity, we declare "throws Exception".
     Real programs will usually want finer-grained exception handling. */
  public void close() {
    try {
      jmxc.close();
    } catch (IOException e) {
      log.error("Unable to close JMX connector", e);
    }
    log.info("JMX connector successfully closed");
  }

}
