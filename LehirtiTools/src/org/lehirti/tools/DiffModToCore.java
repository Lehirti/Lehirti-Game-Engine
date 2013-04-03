package org.lehirti.tools;

import java.util.List;

import lge.res.text.TextKey;
import lge.res.text.TextWrapper;
import lge.util.ClassFinder;
import lge.util.PathFinder;

public final class DiffModToCore {
  public static void main(final String[] args) {
    PathFinder.registerContentDir("MAIN");
    final List<Class<?>> textClasses = new ClassFinder().findSubclasses(TextKey.class).get(TextKey.class);
    for (final Class<?> textClass : textClasses) {
      if (textClass.isEnum()) {
        final TextKey[] keys = (TextKey[]) textClass.getEnumConstants();
        for (final TextKey key : keys) {
          new TextWrapper(key, true);
        }
      }
    }
  }
}
