package org.lehirti.engine.progressgraph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

import org.lehirti.engine.Main;
import org.lehirti.engine.util.MiscUtils;

public final class ProgressGraph {
  private final ProgressNode rootNode;
  private final Class<? extends PG> pgClass;
  private int[] widthsPerLevels;
  
  public ProgressGraph(final Class<? extends PG> pgClass) {
    this.pgClass = pgClass;
    final PG[] nodes = pgClass.getEnumConstants();
    if (nodes == null || nodes.length == 0) {
      throw new RuntimeException(pgClass.getName() + " has no constants");
    }
    final Map<PG, ProgressNode> allNodes = new LinkedHashMap<PG, ProgressNode>();
    this.rootNode = new ProgressNode(nodes[0]);
    if (!this.rootNode.hasBeenReached()) {
      this.rootNode.setActive();
    }
    allNodes.put(nodes[0], this.rootNode);
    for (int i = 1; i < nodes.length; i++) {
      final PG pg = nodes[i];
      final ProgressNode node = new ProgressNode(pg);
      allNodes.put(pg, node);
      final PG[] parents = pg.getParents();
      if (parents == null || parents.length == 0) {
        throw new RuntimeException("Non-root node " + pg.getClass() + "." + pg.name() + " without parents.");
      }
      for (final PG parent : parents) {
        final ProgressNode parentNode = allNodes.get(parent);
        parentNode.addChild(node);
      }
    }
    assignPositions(allNodes);
  }
  
  private void assignPositions(final Map<PG, ProgressNode> allNodes) {
    this.widthsPerLevels = new int[this.rootNode.getDepth()];
    for (final ProgressNode node : allNodes.values()) {
      final int level = node.getLevel();
      this.widthsPerLevels[level]++;
      node.setWidthInGraph(this.widthsPerLevels[level]);
    }
  }
  
  public Class<? extends PG> getPG() {
    return this.pgClass;
  }
  
  int getDepth() {
    return this.rootNode.getDepth();
  }
  
  public ProgressNode getActiveNode() {
    return this.rootNode.getActiveNode();
  }
  
  @Override
  public String toString() {
    return ProgressGraph.class.getSimpleName() + "[" + this.rootNode.toString() + "]";
  }
  
  public Image getDynamicGraphImage(final int width, final int height) {
    final BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2d = bimg.createGraphics();
    final int nodeSize = determineNodeSize(width, height, MiscUtils.max(this.widthsPerLevels), getDepth());
    g2d.setStroke(new BasicStroke(nodeSize / 4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    g2d.setFont(Main.getScaledFont());
    this.rootNode.draw(g2d, width, height, this.widthsPerLevels, getDepth(), nodeSize);
    return bimg;
  }
  
  private int determineNodeSize(final int width, final int height, final int maxWIdthPerLevel, final int totalGraphDepth) {
    final int x = width / maxWIdthPerLevel;
    final int y = height / totalGraphDepth;
    return x < y ? x / 2 : y / 2;
  }
}
