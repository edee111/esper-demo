package cz.muni.fi.espmon.teststatementresultrecieving;

/**
 * @author Eduard Tomek
 * @since 9.5.15
 */
public enum ResultRecievingType {
  SUBSCRIBER, LISTENER, ITERATOR;

  public static ResultRecievingType valueOfStr(String str) {
    str = str.toUpperCase();
    switch (str) {
      case "SUBSCRIBER": return SUBSCRIBER;
      case "LISTENER": return LISTENER;
      case "ITERATOR": return ITERATOR;

      default: throw new IllegalArgumentException("Cannot determine ResultRecievingType from " + str);
    }
  }
}
