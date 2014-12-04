package cz.muni.fi;

import cz.muni.fi.runtime.GlobalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Eduard Tomek
 * @since 27.11.14
 */
@Component
public class Core {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private GlobalClient client;


  public void run() {
    log.debug(getStartingMessage());
    
    System.out.println(client);
    client.connect();
    client.getConnectionInfo();
    client.createMBeanProxies();
    client.listen();
  }


  private String getStartingMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n************************************************************");
    sb.append("\n* STARTING *");
    sb.append("\n************************************************************\n");
    return sb.toString();
  }
}
