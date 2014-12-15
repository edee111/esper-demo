package cz.muni.fi;

import cz.muni.fi.runtime.GlobalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
@Component
public class Core {
  private final Logger log = LoggerFactory.getLogger(getClass());
  private static final String[] servers = new String[]{
          "service:jmx:rmi:///jndi/rmi://:9999/jmxrmi",
          "service:jmx:rmi:///jndi/rmi://:9998/jmxrmi"
  };

  private List<GlobalClient> clients = new ArrayList<>();

  @PostConstruct
  public void initialize() {

    for (String serverUrl : servers) {
      GlobalClient globalClient = new GlobalClient();
      globalClient.connect(serverUrl);
      clients.add(globalClient);
    }

  }

  public void run() {
    log.debug(getStartingMessage());
    System.out.println(clients);
    for (GlobalClient c : clients) {
      c.getConnectionInfo();
      c.createMBeanProxies();
      c.listen();
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
