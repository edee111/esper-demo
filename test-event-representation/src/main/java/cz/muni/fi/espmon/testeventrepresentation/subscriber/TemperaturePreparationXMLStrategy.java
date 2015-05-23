package cz.muni.fi.espmon.testeventrepresentation.subscriber;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eduard Tomek
 * @since 17.4.15
 */
public class TemperaturePreparationXMLStrategy implements TemperaturePreparationStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] prepareData(Object[] temps) {
    for (int i = 0; i < temps.length; i++) {
      Node tempNode = (Node) temps[i];
      NamedNodeMap atts = tempNode.getAttributes();
      Map<String, String> res = new HashMap();
      for (int j = 0; j < atts.getLength(); j++) {
        Node item = atts.item(j);
        res.put(item.getNodeName(), item.getNodeValue());
      }

      temps[i] = res;
    }

    return temps;
  }
}
