package org.lehirti.engine.res.images;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.res.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * meant to be implemented by Enums
 */
public interface ImageKey extends ResourceKey {
  public static class IO {
    private static final Logger LOGGER = LoggerFactory.getLogger(IO.class);
    
    public static void write(final ImageKey key, final ObjectOutput out) throws IOException {
      out.writeObject(key.getClass().getName());
      out.writeObject(key.name());
    }
    
    public static ImageKey read(final ObjectInput in) throws ClassNotFoundException, IOException {
      final String className = (String) in.readObject();
      final String key = (String) in.readObject();
      try {
        return (ImageKey) Enum.valueOf((Class<? extends Enum>) Class.forName(className), key);
      } catch (final ClassNotFoundException e) {
        LOGGER.warn("Failed to reconstruct ImageKey from className " + className + " and key " + key, e);
      }
      return null;
    }
  }
}
