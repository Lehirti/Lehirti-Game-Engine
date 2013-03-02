package org.lehirti.tools;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lehirti.engine.state.AbstractState;
import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.StringState;
import org.lehirti.engine.util.ClassFinder;
import org.lehirti.engine.util.FileUtils;
import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.NPCCommonStats;

public final class GenerateNPCCommon {
  private static final String PREFIX_NPCCommon = "  // BEGIN GENERATED BLOCK NPCCommon";
  private static final String POSTFIX_NPCCommon = "  // END GENERATED BLOCK NPCCommon";
  private static final Pattern NPCCommon_TO_BE_REPLACED = Pattern.compile(PREFIX_NPCCommon + ".*?" + POSTFIX_NPCCommon,
      Pattern.DOTALL);
  
  public static void main(final String[] args) {
    final File root = new File(args[0]);
    if (!root.isDirectory()) {
      System.err.println("invalid root dir: " + args[0]);
      return;
    }
    
    // only update final classes that implement the NPC interface
    final Vector<Class<?>> npcs = new ClassFinder().findSubclasses(NPC.class.getName());
    for (final Class<?> npc : npcs) {
      if (Modifier.isFinal(npc.getModifiers())) {
        updateSource(npc.getName(), root);
      }
    }
  }
  
  private static void updateSource(final String fqcn, final File root) {
    final File sourceFile = new File(root, fqcn.replaceAll("\\.", File.separator) + ".java");
    if (!sourceFile.isFile()) {
      System.err.println(sourceFile.getAbsolutePath() + " is not a file");
    }
    String value = FileUtils.readContentAsString(sourceFile);
    value = transform(value);
    FileUtils.writeContentToFile(sourceFile, value);
  }
  
  private static String transform(final String npcSourceFileContent) {
    final Matcher matcher = NPCCommon_TO_BE_REPLACED.matcher(npcSourceFileContent);
    if (matcher.find()) {
      final String matchingBlock = matcher.group();
      final String newGeneratedBlock = buildGeneratedPart(matchingBlock);
      return matcher.replaceAll(newGeneratedBlock);
    } else {
      System.err.println("No markers " + NPCCommon_TO_BE_REPLACED.toString() + " found in\n" + npcSourceFileContent);
      return npcSourceFileContent;
    }
  }
  
  private static String buildGeneratedPart(final String matchingBlock) {
    final StringBuilder sb = new StringBuilder(PREFIX_NPCCommon + "\n");
    create(sb, matchingBlock, "String", StringState.class);
    create(sb, matchingBlock, "Int", IntState.class);
    create(sb, matchingBlock, "Bool", BoolState.class);
    sb.append(POSTFIX_NPCCommon);
    return sb.toString();
  }
  
  private static void create(final StringBuilder sb, final String matchingBlock, final String key,
      final Class<? extends AbstractState> stateClass) {
    sb.append("  public static enum " + key + " implements " + key + "State {\n");
    for (final NPCCommonStats stat : NPCCommonStats.values()) {
      if (stateClass.equals(stat.type)) {
        sb.append("    " + stat.name() + ",\n");
      }
    }
    final String prefix = "    // BEGIN MANUAL BLOCK " + key;
    final String postfix = "    // END MANUAL BLOCK " + key;
    final Pattern local_TO_BE_REPLACED = Pattern.compile(prefix + ".*?" + postfix, Pattern.DOTALL);
    final Matcher matcher = local_TO_BE_REPLACED.matcher(matchingBlock);
    if (matcher.find()) {
      sb.append(matcher.group());
      sb.append("\n");
    } else {
      sb.append("    // BEGIN MANUAL BLOCK " + key + "\n");
      sb.append("    // END MANUAL BLOCK " + key + "\n");
    }
    sb.append("  }\n");
    sb.append("  \n");
  }
}
