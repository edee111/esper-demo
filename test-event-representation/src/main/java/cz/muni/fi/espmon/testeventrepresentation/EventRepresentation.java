package cz.muni.fi.espmon.testeventrepresentation;

/**
 * @author Eduard Tomek
 * @since 15.4.15
 */
public enum EventRepresentation {
    POJO, MAP, ARRAY, XML;

  /**
   * Get EventRepresentation value from string case insensitive.
   *
   * @param str input string
   * @return EventRepresentation value
   */
    public static EventRepresentation valueOfStr(String str) {
      str = str.toUpperCase();
      switch (str) {
        case "POJO": return POJO;
        case "MAP": return MAP;
        case "ARRAY": return ARRAY;
        case "XML": return XML;

        default: throw new IllegalArgumentException("Cannot determine EventRepresentation from " + str);
      }
    }
}
