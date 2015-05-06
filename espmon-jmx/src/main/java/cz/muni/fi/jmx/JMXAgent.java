package cz.muni.fi.jmx;



import cz.muni.fi.EspmonJMXException;
import cz.muni.fi.MBean;
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
  private static final String DEFAULT_MBEAN_SERVER_PORT = "9999";

  //environment properties for enabling jmx support
  private static final String[] ENV_PROPS_KEYS = {
      "com.sun.management.jmxremote.",
      "com.sun.management.jmxremote.ssl",
      "com.sun.management.jmxremote.authenticate",
      "com.sun.management.jmxremote.port",
      "com.sun.management.jmxremote.local.only",
  };
  private static final String[] ENV_PROPS_VALUE_DEFAULTS = {"", "false", "false", DEFAULT_MBEAN_SERVER_PORT, "false"};

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

  private JMXAgent() throws IOException {
    this.mbs = ManagementFactory.getPlatformMBeanServer();
    loadJMXAgent(Integer.valueOf(DEFAULT_MBEAN_SERVER_PORT), mbs);
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

    log.info("Initializing the environment map");
    LocateRegistry.createRegistry(port);
    Map<String, Object> env = new HashMap<>();
    for (int i = 0; i < ENV_PROPS_KEYS.length; i++) {
      if (System.getProperty(ENV_PROPS_KEYS[i]) == null) {
        env.put(ENV_PROPS_KEYS[i], ENV_PROPS_VALUE_DEFAULTS[i]);
      }
    }

    log.info("Creating an RMI connector server");
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
    cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);

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
      cs.stop();
      instance = null;
    } catch (IOException e) {
      throw new EspmonJMXException("Cannot stopEsperMetricsMonitoring JMX connector server", e);
    }
  }
}