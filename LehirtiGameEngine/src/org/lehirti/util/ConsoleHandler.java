package org.lehirti.util;

public class ConsoleHandler extends java.util.logging.ConsoleHandler {
  public ConsoleHandler() {
    setOutputStream(System.out);
  }
}
