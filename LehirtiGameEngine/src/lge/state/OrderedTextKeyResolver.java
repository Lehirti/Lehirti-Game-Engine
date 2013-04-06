package lge.state;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import lge.res.text.OrderedTextKey;

public final class OrderedTextKeyResolver {
  /**
   * @param value
   * @param textKeys
   * @return textKey with the largest startValue <= value
   */
  public static OrderedTextKey resolve(final long value, final OrderedTextKey[] textKeys) {
    final SortedMap<Long, OrderedTextKey> sortedMap = new TreeMap<>();
    for (final OrderedTextKey key : textKeys) {
      sortedMap.put(Long.valueOf(key.getStartValue()), key);
    }
    OrderedTextKey prev = null;
    for (final Map.Entry<Long, OrderedTextKey> entry : sortedMap.entrySet()) {
      if (entry.getKey().longValue() == value) {
        return entry.getValue();
      } else if (entry.getKey().longValue() > value) {
        return prev;
      } else /* entry.getKey().longValue() < value */{
        prev = entry.getValue();
      }
    }
    return prev;
  }
}
