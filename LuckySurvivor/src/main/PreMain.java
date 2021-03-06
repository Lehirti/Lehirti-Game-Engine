package main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import lge.util.FileUtils;
import lge.util.LogUtils;
import lge.util.PathFinder;
import lge.xmlevents.XMLEventsHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PreMain {
  static {
    final File logsDir = new File("logs");
    if (!logsDir.exists() && !logsDir.mkdirs()) {
      System.err.println("Could not create logs dir " + logsDir.getAbsolutePath());
    }
    LogUtils.createDefaultLogConfigFileIfMissing();
    System.setProperty("java.util.logging.config.file", "config/logging.properties");
    
    FileUtils.copyFile(PreMain.class.getResourceAsStream("/lge/xmlevents/schema/event.xsd"), PathFinder.EVENT_XSD);
  }
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PreMain.class);
  
  /**
   * @param args
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException {
    final List<String> newArgs = new LinkedList<>();
    newArgs.add(determineJavaExecutable());
    newArgs.add("-cp");
    newArgs.add("mod/events/bin" + determineSeparator() + determineNameOfOwnJar());
    for (final String arg : args) {
      newArgs.add(arg);
    }
    createLaunchFile(newArgs);
  }
  
  private static void createLaunchFile(final List<String> args) {
    if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1) {
      createWinLaunchFile(args);
    } else {
      createUnixoidLaunchFile(args);
    }
  }
  
  private static void createUnixoidLaunchFile(final List<String> args) {
    final StringBuilder sb = new StringBuilder("#!/bin/bash");
    sb.append(FileUtils.NL);
    boolean first = true;
    for (final String arg : args) {
      if (!first) {
        sb.append(" ");
      } else {
        first = false;
      }
      sb.append(escape(arg));
    }
    sb.append(" ");
    sb.append(XMLEventsHelper.class.getName());
    sb.append(FileUtils.NL);
    first = true;
    for (final String arg : args) {
      if (!first) {
        sb.append(" ");
      } else {
        first = false;
      }
      sb.append(escape(arg));
    }
    sb.append(" ");
    sb.append(Main.class.getName());
    sb.append(FileUtils.NL);
    
    final String ownNameJar = determineNameOfOwnJar();
    final File launchFile = new File(ownNameJar.substring(0, ownNameJar.length() - ".jar".length()) + ".sh")
        .getAbsoluteFile();
    FileUtils.writeContentToFile(launchFile, sb.toString());
    final boolean isExecutable = launchFile.setExecutable(true);
    if (!isExecutable) {
      LOGGER.warn("Failed to make launch file \"{}\" executable. You may have to do it yourself.",
          launchFile.getAbsolutePath());
    }
    LOGGER.info("Created launch file \"{}\" for the main game. This is just a small shell script.",
        launchFile.getName());
  }
  
  private static void createWinLaunchFile(final List<String> args) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (final String arg : args) {
      if (!first) {
        sb.append(" ");
      } else {
        first = false;
      }
      sb.append(escape(arg));
    }
    sb.append(" ");
    sb.append(XMLEventsHelper.class.getName());
    sb.append(FileUtils.NL);
    first = true;
    for (final String arg : args) {
      if (!first) {
        sb.append(" ");
      } else {
        first = false;
      }
      sb.append(escape(arg));
    }
    sb.append(" ");
    sb.append(Main.class.getName());
    sb.append(FileUtils.NL);
    sb.append("pause");
    sb.append(FileUtils.NL);
    
    final String ownNameJar = determineNameOfOwnJar();
    final File launchFile = new File(ownNameJar.substring(0, ownNameJar.length() - ".jar".length()) + ".bat")
        .getAbsoluteFile();
    FileUtils.writeContentToFile(launchFile, sb.toString());
    LOGGER.info("Created launch file \"{}\" for the main game. This is just a small batch file.", launchFile.getName());
  }
  
  private static String escape(final String arg) {
    if (arg.indexOf(" ") < 0) {
      return arg;
    } else {
      return "\"" + arg + "\"";
    }
  }
  
  private static String determineNameOfOwnJar() {
    return new File(PreMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
  }
  
  private static String determineJavaExecutable() {
    final String javaHome = System.getProperty("java.home");
    if (javaHome == null) {
      LOGGER.error("System property \"java.home\" not set; unable to launch game automatically.");
      printUsageAndExit();
    }
    File f = new File(javaHome);
    if (!f.exists()) {
      LOGGER.error("System property \"java.home\" points to non-existing path \"{}\".", f.getAbsolutePath());
      printUsageAndExit();
    }
    f = new File(f, "bin");
    if (!f.exists()) {
      LOGGER.error("\"bin\" directory \"{}\" in system property \"java.home\" does not exist.", f.getAbsolutePath());
      printUsageAndExit();
    }
    File executable = new File(f, "javaw.exe");
    if (executable.exists()) {
      return executable.getAbsolutePath();
    }
    executable = new File(f, "java.exe");
    if (executable.exists()) {
      return executable.getAbsolutePath();
    }
    executable = new File(f, "java");
    if (executable.exists()) {
      return executable.getAbsolutePath();
    }
    
    LOGGER.error("Java executable not found in \"{}\".", f.getAbsolutePath());
    printUsageAndExit();
    return null;
  }
  
  private static void printUsageAndExit() {
    
    LOGGER.info("Try: <path_to_jdk_java_executable> -cp mod/events/bin" + determineSeparator()
        + "LuckySurvivor-XY.jar main.Main");
    System.exit(-1);
  }
  
  private static String determineSeparator() {
    String separator;
    if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1) {
      separator = ";";
    } else {
      separator = ":";
    }
    return separator;
  }
}
