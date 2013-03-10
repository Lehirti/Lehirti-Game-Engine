package lge.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lge.state.AbstractState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);
  
  private static final Map<String, Properties> CACHE = new HashMap<>();
  
  public static Properties getDefaultProperties(final Class<? extends AbstractState> stateClass) {
    final Properties properties = CACHE.get(stateClass.getName());
    if (properties != null) {
      return properties;
    }
    
    // start with empty properties
    final Properties defaultProperties = new Properties();
    try {
      
      // mod folder first
      final File modFolderPropertiesFile = PathFinder.getModDefaultProperties(stateClass);
      if (modFolderPropertiesFile.isFile()) {
        FileInputStream fis = null;
        try {
          fis = new FileInputStream(modFolderPropertiesFile);
          defaultProperties.load(fis);
          return defaultProperties;
        } catch (final FileNotFoundException e) {
          LOGGER.error("Existing file " + modFolderPropertiesFile.getAbsolutePath() + " not found.", e);
        } catch (final IOException e) {
          LOGGER.error("Could not read from " + modFolderPropertiesFile.getAbsolutePath(), e);
        } finally {
          if (fis != null) {
            try {
              fis.close();
            } catch (final IOException e) {
              LOGGER.error("Error closing file input stream " + fis, e);
            }
          }
        }
      }
      
      // if not found: try inside jar
      final String nameInJar = "/" + stateClass.getName().replaceAll("\\.", "/")
          + PathFinder.PROPERTIES_FILENAME_SUFFIX;
      final InputStream is = PropertyUtils.class.getResourceAsStream(nameInJar);
      if (is != null) {
        try {
          try {
            defaultProperties.load(is);
            return defaultProperties;
          } catch (final IOException e) {
            LOGGER.error("Error reading default values from props file in jar " + is, e);
          }
        } finally {
          try {
            is.close();
          } catch (final IOException e) {
            LOGGER.error("Error closing " + is, e);
          }
        }
      }
      
      // (for development environment): try in src dir
      final File src = new File("src");
      if (src.isDirectory()) {
        final String packageName = stateClass.getPackage().getName();
        File packageDir = src;
        for (final String dir : packageName.split("\\.")) {
          packageDir = new File(packageDir, dir);
        }
        if (packageDir.isDirectory()) {
          final String fullSimpleName = stateClass.getName().substring(packageName.length() + 1)
              + PathFinder.PROPERTIES_FILENAME_SUFFIX;
          final File defaultPropsFileInSrc = new File(packageDir, fullSimpleName);
          if (defaultPropsFileInSrc.isFile()) {
            FileInputStream fis = null;
            try {
              fis = new FileInputStream(defaultPropsFileInSrc);
              defaultProperties.load(fis);
              return defaultProperties;
            } catch (final FileNotFoundException e) {
              LOGGER.error("Cannot read from exising file " + defaultPropsFileInSrc.getAbsolutePath(), e);
            } catch (final IOException e) {
              LOGGER.error("Error writing to " + defaultPropsFileInSrc.getAbsolutePath(), e);
            } finally {
              if (fis != null) {
                try {
                  fis.close();
                } catch (final IOException e) {
                  LOGGER.error("Error closing file input stream " + fis, e);
                }
              }
            }
          }
        }
      }
      
      // if default properties are nowhere to be found: return empty properties
      return defaultProperties;
    } finally {
      CACHE.put(stateClass.getName(), defaultProperties);
    }
  }
  
  public static void setDefaultProperties(final Class<? extends AbstractState> stateClass,
      final Properties defaultProperties) {
    CACHE.put(stateClass.getName(), defaultProperties);
    
    // in development environment: write to props file to src dir
    final File src = new File("src");
    if (src.isDirectory()) {
      final String packageName = stateClass.getPackage().getName();
      File packageDir = src;
      for (final String dir : packageName.split("\\.")) {
        packageDir = new File(packageDir, dir);
      }
      if (packageDir.isDirectory()) {
        final String fullSimpleName = stateClass.getName().substring(packageName.length() + 1)
            + PathFinder.PROPERTIES_FILENAME_SUFFIX;
        final File defaultPropsFileInSrc = new File(packageDir, fullSimpleName);
        FileOutputStream fos = null;
        try {
          fos = new FileOutputStream(defaultPropsFileInSrc);
          defaultProperties.store(fos, null);
          return;
        } catch (final FileNotFoundException e) {
          LOGGER.error("Cannot write to " + defaultPropsFileInSrc.getAbsolutePath(), e);
        } catch (final IOException e) {
          LOGGER.error("Error writing to " + defaultPropsFileInSrc.getAbsolutePath(), e);
        } finally {
          if (fos != null) {
            try {
              fos.close();
            } catch (final IOException e) {
              LOGGER.error("Error closing file output stream " + fos, e);
            }
          }
        }
      }
    }
    
    // in regular game, write to mod folder
    final File modFolderPropertiesFile = PathFinder.getModDefaultProperties(stateClass);
    final File modFolderDefaultValuesFolder = modFolderPropertiesFile.getParentFile();
    if (!modFolderDefaultValuesFolder.isDirectory() && !modFolderDefaultValuesFolder.mkdirs()) {
      LOGGER.error("Failed to create \"default values\" dir in mod dir: "
          + modFolderDefaultValuesFolder.getAbsolutePath());
      return;
    }
    
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(modFolderPropertiesFile);
      defaultProperties.store(fos, null);
    } catch (final FileNotFoundException e) {
      LOGGER.error("Cannot write to " + modFolderPropertiesFile.getAbsolutePath(), e);
    } catch (final IOException e) {
      LOGGER.error("Error writing to " + modFolderPropertiesFile.getAbsolutePath(), e);
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (final IOException e) {
          LOGGER.error("Error closing file output stream " + fos, e);
        }
      }
    }
  }
}
