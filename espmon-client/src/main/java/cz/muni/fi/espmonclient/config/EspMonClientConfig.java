package cz.muni.fi.espmonclient.config;

import cz.muni.fi.espmonclient.EspmonClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for configuration management of espmon-client
 *
 * @author Eduard Tomek
 * @since 6.2.15
 */
@Component
public class EspMonClientConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Value("#{environment.ESPMON_HOME}")
  private String ESPMON_HOME;

  private final String DIR_CONF = "conf";
  private final String DIR_LOGS = "logs";
  private final String FILE_ESPMON_XML = "espmon.xml";
  private final String FILE_LOGBACK_XML = "logback.xml";
  private final String FILE_SEPARATOR = System.getProperty("file.separator");
  private String PATH_ESPMON_HOME_CONF;
  private String PATH_ESPMON_XML;
  private String PATH_LOGBACK_XML;
  private String PATH_LOGS_DIR;

  private final String ELEMENT_SERVER = "server";
  private final String ATTRIBUTE_URI = "uri";

  private final String FILE_ESPMON_XSD_SCHEMA = "espmon.xsd";

  @PostConstruct
  public void init() {
    PATH_ESPMON_HOME_CONF = ESPMON_HOME + FILE_SEPARATOR + DIR_CONF;
    PATH_LOGBACK_XML = PATH_ESPMON_HOME_CONF + FILE_SEPARATOR + FILE_LOGBACK_XML;
    PATH_ESPMON_XML = PATH_ESPMON_HOME_CONF + FILE_SEPARATOR + FILE_ESPMON_XML;
    PATH_LOGS_DIR = ESPMON_HOME + FILE_SEPARATOR + DIR_LOGS + FILE_SEPARATOR;
  }

  /**
   * Get list of JMX server URI's from configuration file
   *
   * @return list of server URI's
   * @throws cz.muni.fi.espmonclient.EspmonClientException if configuration file doesn't exist or is not well formed
   */
  public List<String> getServers() throws EspmonClientException {
    log.info("Aquiring servers from config.");
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc = null;
    File configFile = new File(PATH_ESPMON_XML);
    validateConfigFile(configFile);
    try {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(configFile);
    } catch (SAXException | IOException | ParserConfigurationException e) {
        throw new EspmonClientException("Cannot aquire server config file", e);
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

  public String getLogsDirPath() {
    return PATH_LOGS_DIR;
  }

  /**
   * Validate config file to XSD schema
   *
   * @param configFile config file
   * @throws cz.muni.fi.espmonclient.EspmonClientException if config file is not valid
   */
  private void validateConfigFile(File configFile) throws EspmonClientException {
    Source xmlFile = new StreamSource(configFile);
    URL xsdSchemaUrl = this.getClass().getClassLoader().getResource(FILE_ESPMON_XSD_SCHEMA);
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    try {
      Schema schema = schemaFactory.newSchema(xsdSchemaUrl);
      Validator validator = schema.newValidator();
      validator.validate(xmlFile);
    } catch (IOException | SAXException e) {
      log.error("EspMon config file is not valid");
      throw new EspmonClientException("EspMon config file is not valid to schema.", e);
    }

    log.info("Validation of espmon client config file was successful.");
  }
}