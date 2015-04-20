package cz.muni.fi;

import cz.muni.fi.config.EspMonConfig;
import cz.muni.fi.runtime.JMXClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
@Component
public class Core {
  private final Logger log = LoggerFactory.getLogger(getClass());
  @Autowired
  private EspMonConfig config;

  private List<String> servers = new ArrayList<>();
  private List<JMXClient> clients = new ArrayList<>();

  @PostConstruct
  public void initialize() {
    log.info(getInitializingMessage());
    try {
      servers = config.getServers();
    } catch (EspMonException e) {
      log.error("Server configuration load failed", e);
    }

    for (String serverUrl : servers) {
      JMXClient JMXClient = new JMXClient();
      try {
        JMXClient.connect(serverUrl);
        clients.add(JMXClient);
      } catch (IOException e) {
        log.error("JMXClient could not connect to server " + serverUrl, e);
      }
    }
  }

  public void run() {
    log.info(getStartingMessage());
    if (clients.isEmpty()) {
      log.error("No clients are connected - exiting.");
      return;
    }
    for (JMXClient c : clients) {
      log.info("Creating MBean proxy for " + c.getUrl());
      c.createMBeanProxies();
    }
    listen();
  }

  public void listen() {
    while (true) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        log.error("Exception while sleeping", e);
        System.exit(1);
      }
    }
  }

  @PreDestroy
  public void stop() {
    for (JMXClient c : clients) {
      c.close();
    }
  }

  private String getStartingMessage() {
    return getInfoMessage("STARTING");
  }

  private String getInitializingMessage() {
    return getInfoMessage("INITIALIZING");
  }

  private String getInfoMessage(String info) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n************************************************************");
    sb.append("\n* " + info + " *");
    sb.append("\n************************************************************\n");
    return sb.toString();
  }
}
