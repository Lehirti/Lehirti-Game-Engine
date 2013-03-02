package org.lehirti.tools;

import java.io.File;

import org.lehirti.engine.util.FileUtils;

public final class MoveModToCore {
  
  /**
   * @param args
   */
  public static void main(final String[] args) {
    if (args.length != 1) {
      System.err.println("Root dir missing");
      return;
    }
    final File rootDir = new File(args[0]);
    if (!rootDir.isDirectory()) {
      System.err.println(args[0] + " is not a directory");
      return;
    }
    final File modDir = new File(rootDir, "mod");
    if (!modDir.isDirectory()) {
      System.err.println(modDir.getAbsolutePath() + " is not a directory");
      return;
    }
    final File coreDir = new File(rootDir, "core");
    if (!coreDir.isDirectory()) {
      System.err.println(coreDir.getAbsolutePath() + " is not a directory");
      return;
    }
    System.out.println("Moving all files from " + modDir.getAbsolutePath() + " to " + coreDir.getAbsolutePath());
    moveContentsRec(modDir, coreDir);
    System.out.println("Done");
  }
  
  private static void moveContentsRec(final File src, final File dest) {
    for (final File oneSrc : src.listFiles()) {
      final File destFile = new File(dest, oneSrc.getName());
      if (oneSrc.isFile()) {
        System.out.println(oneSrc.getAbsolutePath() + " -> " + destFile.getAbsolutePath());
        if (!FileUtils.copyFile(oneSrc, destFile)) {
          System.err.println("Failed to move " + oneSrc.getAbsolutePath() + " to " + destFile.getAbsolutePath());
          System.exit(-1);
        } else {
          if (!oneSrc.delete()) {
            System.err.println("Failed to delete " + oneSrc.getAbsolutePath());
          }
        }
      } else if (oneSrc.isDirectory()) {
        if (oneSrc.getName().equals("defaults")) {
          continue;
        }
        if (!destFile.exists()) {
          if (!destFile.mkdir()) {
            System.err.println("Failed to create " + destFile.getAbsolutePath());
            System.exit(-1);
          }
        }
        moveContentsRec(oneSrc, destFile);
        if (!oneSrc.delete()) {
          System.err.println("Failed to delete " + oneSrc.getAbsolutePath());
        }
      }
    }
    
  }
  
}
