package org.lehirti.engine.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * do not use logger here
 */
public class LogUtils {
  
  public static void createDefaultLogConfigFileIfMissing() {
    final File configDir = new File("config");
    final File logConfigFile = new File(configDir, "logging.properties");
    if (!logConfigFile.isFile()) {
      if (!configDir.isDirectory()) {
        if (!configDir.mkdirs()) {
          System.err.println("Cannot create config dir " + configDir.getAbsolutePath());
          return;
        }
      }
      
      final String logFileContent = //
      "handlers=java.util.logging.FileHandler, org.lehirti.engine.util.ConsoleHandler\n" + //
          "# Default global logging level.\n" + //
          "# Loggers and Handlers may override this level\n" + //
          ".level=INFO\n" + //
          "org.lehirti.level=FINE\n" + //
          "org.atrun.level=FINE\n" + //
          "org.lehirti.engine.util.ConsoleHandler.formatter=org.lehirti.engine.util.ConsoleLogFormatter\n" + //
          "org.lehirti.engine.util.ConsoleHandler.level=INFO\n" + //
          "# --- FileHandler ---\n" + //
          "java.util.logging.FileHandler.level=FINE\n" + //
          "# log file location\n" + //
          "java.util.logging.FileHandler.pattern=logs/all.log\n" + //
          "# Limiting size of output file in bytes:\n" + //
          "java.util.logging.FileHandler.limit=2500000\n" + //
          "# Number of output files to cycle through, by appending an\n" + //
          "# integer to the base file name:\n" + //
          "java.util.logging.FileHandler.count=5\n" + //
          "java.util.logging.FileHandler.formatter=org.lehirti.engine.util.LogFileFormatter\n" + //
          "java.util.logging.FileHandler.append=true\n"; //
      
      BufferedWriter writer = null;
      try {
        writer = new BufferedWriter(new FileWriter(logConfigFile));
        writer.write(logFileContent);
      } catch (final IOException e) {
        System.err.println("Failed to write to file " + logConfigFile.getAbsolutePath() + " " + e);
      } finally {
        try {
          if (writer != null) {
            writer.close();
          }
        } catch (final IOException e) {
          System.err.println("Failed to close writer for file " + logConfigFile.getAbsolutePath() + " " + e);
        }
      }
    }
  }
}
