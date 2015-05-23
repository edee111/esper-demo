package cz.muni.fi.espmon.testeventrepresentation.monitor;

import cz.muni.fi.espmon.testeventrepresentation.handler.TemperatureEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class SendXMLEventStrategy implements SendEventStrategy {

  private static final Logger log = LoggerFactory.getLogger(SendXMLEventStrategy.class);

  private final String XML_EVENT_FORMAT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
          "<TemperatureEvent temperature=\"%s\" timeOfReading=\"%s\" serverName=\"%s\" />";
  private final SimpleDateFormat sdfISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
  private DocumentBuilder builder;

  public SendXMLEventStrategy() {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      this.builder = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      log.error("Cannot create document builder", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTemperatureEvent(Integer temp, Date date, String serverName) {
    String xmlString = String.format(XML_EVENT_FORMAT, temp, sdfISO8601.format(date), serverName);
    try {
      Document eventNode;
      synchronized (builder) {
         eventNode = builder.parse(new InputSource(new StringReader(xmlString)));
      }
      TemperatureEventHandler.handle(eventNode);
    } catch (SAXException | IOException e) {
      log.error("Cannot send event", e);
    }
  }
}
