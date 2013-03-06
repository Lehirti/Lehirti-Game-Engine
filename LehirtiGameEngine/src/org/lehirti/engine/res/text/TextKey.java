package org.lehirti.engine.res.text;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lehirti.engine.res.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * meant to be implemented by Enums
 */
public interface TextKey extends ResourceKey {
  public static class IO {
    private static final Logger LOGGER = LoggerFactory.getLogger(IO.class);
    
    public static void write(final @Nullable TextKey key, final @Nonnull ObjectOutput out) throws IOException {
      if (key == null) {
        out.writeObject(null); // input is null
      } else {
        out.writeObject(key.getClass().getName());
        out.writeObject(key.name());
      }
    }
    
    @SuppressWarnings("unchecked")
    @CheckForNull
    public static TextKey read(final @Nonnull ObjectInput in) throws ClassNotFoundException, IOException {
      final String className = (String) in.readObject();
      if (className == null) {
        // input was null
        return null;
      }
      
      final String key = (String) in.readObject();
      try {
        return (TextKey) Enum.valueOf((Class<? extends Enum>) Class.forName(className), key);
      } catch (final ClassNotFoundException | ClassCastException e) {
        LOGGER.warn("Failed to reconstruct TextKey from className " + className + " and key " + key, e);
      }
      return null;
    }
  }
}
