package org.lehirti.tools;

import java.io.File;
import java.util.regex.Pattern;

import org.lehirti.engine.util.FileUtils;
import org.lehirti.luckysurvivor.sss.SexAct;

public final class UpdateGeneratedCodeBlocks {
  private static final String PREFIX = "// BEGIN GENERATED BLOCK SexAct";
  private static final String POSTFIX = "// END GENERATED BLOCK SexAct";
  private static final Pattern TO_BE_REPLACED = Pattern.compile(PREFIX + ".*?" + POSTFIX, Pattern.DOTALL);
  private static final String REPLACE_WITH;
  static {
    final String NEWLINE = System.getProperty("line.separator") + "    ";
    final StringBuilder sb = new StringBuilder();
    sb.append(PREFIX);
    sb.append(NEWLINE);
    for (final SexAct act : SexAct.values()) {
      sb.append(act.name());
      sb.append(",");
      sb.append(NEWLINE);
    }
    sb.append(POSTFIX);
    
    REPLACE_WITH = sb.toString();
  }
  
  public static void main(final String[] args) {
    final File root = new File(args[0]);
    if (!root.isDirectory()) {
      System.err.println("invalid root dir: " + args[0]);
      return;
    }
    updateRecursive(root);
  }
  
  private static void updateRecursive(final File dir) {
    for (final File file : dir.listFiles()) {
      if (file.isDirectory()) {
        updateRecursive(file);
      } else if (file.isFile()) {
        updateFile(file);
      }
    }
  }
  
  private static void updateFile(final File file) {
    final String fileContent = FileUtils.readContentAsString(file);
    final String newFileContent = TO_BE_REPLACED.matcher(fileContent).replaceAll(REPLACE_WITH);
    if (!fileContent.equals(newFileContent)) {
      FileUtils.writeContentToFile(file, newFileContent);
    }
  }
}
