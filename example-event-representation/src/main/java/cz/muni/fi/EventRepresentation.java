package cz.muni.fi;

/**
 * @author Eduard Tomek
 * @since 15.4.15
 */
public enum EventRepresentation {
    POJO, MAP, ARRAY, XML;

    public static EventRepresentation valueOfStr(String str) {
      str = str.toUpperCase();
      final String pojoString = POJO.name();
      switch (str) {
        case "POJO": return POJO;
        case "MAP": return MAP;
        case "ARRAY": return ARRAY;
        case "XML": return XML;

        default: throw new IllegalArgumentException("Cannot determine EventRepresentation from " + str);
      }
    }
}
