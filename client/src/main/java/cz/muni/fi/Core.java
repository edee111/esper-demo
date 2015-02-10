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
    try {
      servers = config.getServers();
    } catch (EspMonException e) {
      log.error("Server configuration load failed", e);
    }

    for (String serverUrl : servers) {
      JMXClient JMXClient = new JMXClient();
      JMXClient.connect(serverUrl);
      clients.add(JMXClient);
    }

  }

  public void run() {
    log.info(getStartingMessage());
    for (JMXClient c : clients) {
      c.createMBeanProxies();
    }
    listen();
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

  @PreDestroy
  public void stop() {
    for (JMXClient c : clients) {
      c.close();
    }
  }


  private String getStartingMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n************************************************************");
    sb.append("\n* STARTING *");
    sb.append("\n************************************************************\n");
    return sb.toString();
  }
}
