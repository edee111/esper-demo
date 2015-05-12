package cz.muni.fi.jmx;



import cz.muni.fi.EspmonJMXException;
import cz.muni.fi.espmon.MBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 * JMX agent for publishing MBeans
 *
 * @author Eduard Tomek
 * @since 30.10.14
 */
public class JMXAgent {
  protected Logger log = LoggerFactory.getLogger(getClass());

  private MBeanServer mbs = null;
  JMXConnectorServer cs = null;
  private Map<String, MBean> registeredMBeans = new HashMap<>();
  private static int mbeanServerPort = 9999;

  private static final String JMXREMOTE_CLASS = "com.sun.management.jmxremote";

  private static JMXAgent instance;

  public static JMXAgent getInstance() {
    if (instance == null) {
      try {
        instance = new JMXAgent();
      } catch (IOException e) {
        LoggerFactory.getLogger(JMXAgent.class).error("Cannot create JMX agent", e);
      }
    }
    return instance;
  }

  public static void setMBeanServerPort(int port) {
    mbeanServerPort = port;
  }

  /**
   * @return Environment properties which will use connector server
   */
  private static Map<String, String> createEnvProps() {
    Map<String, String> props = new HashMap<>();
    props.put("com.sun.management.jmxremote", "");
    props.put("com.sun.management.jmxremote.ssl", "false");
    props.put("com.sun.management.jmxremote.authenticate", "false");
    props.put("com.sun.management.jmxremote.port", String.valueOf(mbeanServerPort));
    props.put("com.sun.management.jmxremote.local.only", "false");

    return props;
  }

  private JMXAgent() throws IOException {
    this.mbs = ManagementFactory.getPlatformMBeanServer();
    loadJMXAgent(Integer.valueOf(mbeanServerPort), mbs);
  }

  /**
   * Sets jmx remote related attributes and creates connection. If attribute is already set by VM arguments
   *    doesn't changes it's value.
   *
   * @param port mBean server required port
   * @param mbs mBean server
   * @throws IOException
   */
  private void loadJMXAgent(int port, MBeanServer mbs) throws IOException {

    for (String p : System.getProperties().stringPropertyNames()) {
      if (p.toLowerCase().contains(JMXREMOTE_CLASS)) {
        log.info("Found jmx remote related property " + p +
                ". Program will use only system properties (port, remoting, ssl)");
        return;
      }
    }

    createCustomConnectorServer(port, mbs);
  }

  /**
   * Create custom connector server and register here MBean server
   *
   * @param port port of connector server
   * @param mbs MBean server
   * @throws IOException
   */
  private void createCustomConnectorServer(int port, MBeanServer mbs) throws IOException {
    log.info("Initializing the environment map");
    LocateRegistry.createRegistry(port);

    log.info("Creating an RMI connector server");
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
    cs = JMXConnectorServerFactory.newJMXConnectorServer(url, createEnvProps(), mbs);

    log.info("Starting the RMI connector server");
    cs.start();
  }

  /**
   * Register MBean on MBean server.
   * If MBean of the given type is already registered update its state.
   *
   * @param mBean MBean to be registered
   */
  public synchronized void register(MBean mBean) {
    try {

      ObjectName name = getObjectName(mBean.getClass());
      if (mbs.isRegistered(name)) {
        MBean registeredMBean = registeredMBeans.get(mBean.getClass().getName());
        registeredMBean.update(mBean);
      }
      else {
        mbs.registerMBean(mBean, name);
        registeredMBeans.put(mBean.getClass().getName(), mBean);
      }

    } catch (MalformedObjectNameException
            | NotCompliantMBeanException
            | InstanceAlreadyExistsException
            | MBeanRegistrationException e) {
      log.error("Cannot register MBean." + mBean.getLogInfo(), e);
    }
  }

  private ObjectName getObjectName(Class clazz) throws MalformedObjectNameException {
    return new ObjectName(clazz.getPackage().getName() + ":type=" + clazz.getSimpleName());
  }

  public void stop() throws EspmonJMXException {
    try {
      if (cs != null) {
        cs.stop();
      }
      instance = null;
    } catch (IOException e) {
      throw new EspmonJMXException("Cannot stop EsperMetricsMonitoring JMX connector server", e);
    }
  }
}