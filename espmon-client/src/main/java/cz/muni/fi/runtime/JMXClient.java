package cz.muni.fi.runtime;

import cz.muni.fi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;

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

  public void connect(String jmxServiceUrl) throws IOException {
    log.debug("Creating an RMI connector client and " +
            "connect it to the RMI connector server " + jmxServiceUrl);

    url = new JMXServiceURL(jmxServiceUrl);
    jmxc = JMXConnectorFactory.connect(url, null);
    mbsc = jmxc.getMBeanServerConnection();

    if (mbsc == null) {
      throw new IOException("Connection to " + url + " was not estabilshed");
    }
  }

  public void createMBeanProxies() {
    createMbeanProxy(EngineMetricMBean.class, "cz.muni.fi:type=EngineMetric");
    createMbeanProxy(StatementMetricMBean.class, "cz.muni.fi:type=StatementMetric");
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

    ClientListener listener = new ClientListener(url);
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

  public void close() {
    try {
      jmxc.close();
    } catch (IOException e) {
      log.error("Unable to close JMX connector", e);
    }
    log.info("JMX connector successfully closed");
  }

  public JMXServiceURL getUrl() {
    return url;
  }
}
