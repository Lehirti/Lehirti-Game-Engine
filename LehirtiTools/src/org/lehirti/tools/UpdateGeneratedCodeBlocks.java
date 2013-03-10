package org.lehirti.tools;

import java.io.File;
import java.util.regex.Pattern;

import lge.util.FileUtils;

import sss.ReactionToSexAct;
import sss.SexAct;

public final class UpdateGeneratedCodeBlocks {
  private static final String PREFIX_SexAct = "// BEGIN GENERATED BLOCK SexAct";
  private static final String POSTFIX_SexAct = "// END GENERATED BLOCK SexAct";
  private static final Pattern SexAct_TO_BE_REPLACED = Pattern.compile(PREFIX_SexAct + ".*?" + POSTFIX_SexAct,
      Pattern.DOTALL);
  private static final String SexAct_REPLACE_WITH;
  static {
    final String NEWLINE = System.getProperty("line.separator") + "    ";
    final StringBuilder sb = new StringBuilder();
    sb.append(PREFIX_SexAct);
    sb.append(NEWLINE);
    for (final SexAct act : SexAct.values()) {
      sb.append(act.name());
      sb.append(",");
      sb.append(NEWLINE);
    }
    sb.append(POSTFIX_SexAct);
    
    SexAct_REPLACE_WITH = sb.toString();
  }
  
  private static final String PREFIX_ReactionToSexAct = "// BEGIN GENERATED BLOCK ReactionToSexAct";
  private static final String POSTFIX_ReactionToSexAct = "// END GENERATED BLOCK ReactionToSexAct";
  private static final Pattern ReactionToSexAct_TO_BE_REPLACED = Pattern.compile(PREFIX_ReactionToSexAct + ".*?"
      + POSTFIX_ReactionToSexAct, Pattern.DOTALL);
  private static final String ReactionToSexAct_REPLACE_WITH;
  static {
    final String NEWLINE = System.getProperty("line.separator") + "    ";
    final StringBuilder sb = new StringBuilder();
    sb.append(PREFIX_ReactionToSexAct);
    sb.append(NEWLINE);
    for (final ReactionToSexAct act : ReactionToSexAct.values()) {
      sb.append(act.name());
      sb.append(",");
      sb.append(NEWLINE);
    }
    sb.append(POSTFIX_ReactionToSexAct);
    
    ReactionToSexAct_REPLACE_WITH = sb.toString();
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
    String newFileContent = SexAct_TO_BE_REPLACED.matcher(fileContent).replaceAll(SexAct_REPLACE_WITH);
    newFileContent = ReactionToSexAct_TO_BE_REPLACED.matcher(newFileContent).replaceAll(ReactionToSexAct_REPLACE_WITH);
    if (!fileContent.equals(newFileContent)) {
      FileUtils.writeContentToFile(file, newFileContent);
    }
  }
}
