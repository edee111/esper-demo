package cz.muni.fi.config;

import cz.muni.fi.EspMonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Tomek
 * @since 6.2.15
 */
@Component
public class EspMonConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Value("#{environment.ESPMON_HOME}")
  private String ESPMON_HOME;

  private final String DIR_CONF = "conf";
  private final String FILE_ESPMON_XML = "espmon.xml";
  private final String FILE_LOGBACK_XML = "logback.xml";
  private final String FILE_SEPARATOR = System.getProperty("file.separator");
  private String PATH_ESPMON_HOME_CONF;
  private String PATH_ESPMON_XML;
  private String PATH_LOGBACK_XML;

  private final String ELEMENT_SERVER = "server";
  private final String ATTRIBUTE_URI = "uri";

  @PostConstruct
  public void init() {
    PATH_ESPMON_HOME_CONF = ESPMON_HOME + FILE_SEPARATOR + DIR_CONF;
    PATH_LOGBACK_XML = PATH_ESPMON_HOME_CONF + FILE_SEPARATOR + FILE_LOGBACK_XML;
    PATH_ESPMON_XML = PATH_ESPMON_HOME_CONF + FILE_SEPARATOR + FILE_ESPMON_XML;
  }

  public List<String> getServers() throws EspMonException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc = null;
    try {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(new File(PATH_ESPMON_XML));
    } catch (SAXException | IOException | ParserConfigurationException e) {
        throw new EspMonException("Cannot aquire server config file", e);
    }

    List<String> serverNames = new ArrayList<>();
    NodeList servers = doc.getElementsByTagName(ELEMENT_SERVER);
    for (int i = 0; i < servers.getLength(); i++) {
        Element e = (Element) servers.item(i);
        serverNames.add(e.getAttribute(ATTRIBUTE_URI));
    }

    return serverNames;
  }

  public String getLogbackXmlPath() {
    return PATH_LOGBACK_XML;
  }
}