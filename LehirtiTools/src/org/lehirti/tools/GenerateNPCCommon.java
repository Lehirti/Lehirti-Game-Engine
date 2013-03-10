package org.lehirti.tools;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lge.state.AbstractState;
import lge.state.BoolState;
import lge.state.IntState;
import lge.state.StringState;
import lge.util.ClassFinder;
import lge.util.FileUtils;

import npc.NPC;
import npc.NPCCommonStats;

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
        updateSource(npc.getName(), root, npc.getSimpleName());
      }
    }
  }
  
  private static void updateSource(final String fqcn, final File root, final String simpleClassName) {
    final File sourceFile = new File(root, fqcn.replaceAll("\\.", File.separator) + ".java");
    if (!sourceFile.isFile()) {
      System.err.println(sourceFile.getAbsolutePath() + " is not a file");
    }
    String value = FileUtils.readContentAsString(sourceFile);
    value = transform(value, simpleClassName);
    FileUtils.writeContentToFile(sourceFile, value);
  }
  
  private static String transform(final String npcSourceFileContent, final String simpleClassName) {
    final Matcher matcher = NPCCommon_TO_BE_REPLACED.matcher(npcSourceFileContent);
    if (matcher.find()) {
      final String matchingBlock = matcher.group();
      final String newGeneratedBlock = buildGeneratedPart(matchingBlock, simpleClassName);
      return matcher.replaceAll(newGeneratedBlock);
    } else {
      System.err.println("No markers " + NPCCommon_TO_BE_REPLACED.toString() + " found in\n" + npcSourceFileContent);
      return npcSourceFileContent;
    }
  }
  
  private static String buildGeneratedPart(final String matchingBlock, final String simpleClassName) {
    final StringBuilder sb = new StringBuilder(PREFIX_NPCCommon + "\n");
    create(sb, matchingBlock, "Str", StringState.class);
    create(sb, matchingBlock, "Int", IntState.class);
    create(sb, matchingBlock, "Bool", BoolState.class);
    createStatLookupTable(sb);
    createTextResolveMethod(sb, simpleClassName);
    sb.append(POSTFIX_NPCCommon);
    return sb.toString();
  }
  
  private static void createTextResolveMethod(final StringBuilder sb, final String simpleClassName) {
    sb.append("  @Override\n");
    sb.append("  public String resolveParameter(final String parameterSuffix) {\n");
    sb.append("    final AbstractState abstractState = STATE_BY_NAME_MAP.get(parameterSuffix);\n");
    sb.append("    if (abstractState == null) {\n");
    sb.append("      return \"[" + simpleClassName + ".resolveParameter(\" + parameterSuffix + \"): UNKNOWN]\";\n");
    sb.append("    }\n");
    sb.append("    return State.get(abstractState);\n");
    sb.append("  }\n");
    sb.append("  \n");
  }
  
  private static void createStatLookupTable(final StringBuilder sb) {
    sb.append("  private final static Map<String, AbstractState> STATE_BY_NAME_MAP = new LinkedHashMap<>();\n");
    sb.append("  static {\n");
    createStatLookupTableForOneType(sb, "Str");
    createStatLookupTableForOneType(sb, "Int");
    createStatLookupTableForOneType(sb, "Bool");
    sb.append("  }\n");
    sb.append("  \n");
  }
  
  private static void createStatLookupTableForOneType(final StringBuilder sb, final String key) {
    sb.append("    for (final AbstractState state : " + key + ".values()) {\n");
    sb.append("      if (STATE_BY_NAME_MAP.containsKey(state.name())) {\n");
    sb.append("        throw new ThreadDeath();\n");
    sb.append("      }\n");
    sb.append("      STATE_BY_NAME_MAP.put(state.name(), state);\n");
    sb.append("    }\n");
  }
  
  private static void create(final StringBuilder sb, final String matchingBlock, final String key,
      final Class<? extends AbstractState> stateClass) {
    sb.append("  public static enum " + key + " implements " + stateClass.getSimpleName() + " {\n");
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
