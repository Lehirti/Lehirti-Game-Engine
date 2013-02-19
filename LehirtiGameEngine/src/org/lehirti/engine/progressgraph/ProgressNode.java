package org.lehirti.engine.progressgraph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.state.State;

public final class ProgressNode {
  private final PG id;
  
  private final List<ProgressNode> parents = new LinkedList<ProgressNode>();
  
  private final List<ProgressNode> children = new LinkedList<ProgressNode>();
  
  private int widthInGraph = 0;
  
  public ProgressNode(final PG id) {
    this.id = id;
  }
  
  void setWidthInGraph(final int width) {
    this.widthInGraph = width;
    
  }
  
  int getWidthInGraph() {
    return this.widthInGraph;
  }
  
  int getLevel() {
    if (this.parents.isEmpty()) {
      return 0; // this is the root node
    }
    int level = Integer.MIN_VALUE;
    for (final ProgressNode parent : this.parents) {
      final int parentLevel = parent.getLevel();
      if (level < parentLevel) {
        level = parentLevel;
      }
    }
    level++;
    return level;
  }
  
  /**
   * @return maximum depth of all its children plus one
   */
  int getDepth() {
    int maxDepth = 0;
    for (final ProgressNode child : this.children) {
      final int childDepth = child.getDepth();
      if (maxDepth < childDepth) {
        maxDepth = childDepth;
      }
    }
    maxDepth++;
    return maxDepth;
  }
  
  void addChild(final ProgressNode child) {
    this.children.add(child);
    child.parents.add(this);
  }
  
  boolean hasBeenReached() {
    return State.is(this.id);
  }
  
  public void setActive() {
    if (this.parents.isEmpty()) {
      // root node can always be set active
      State.set(this.id, true);
    } else {
      // otherwise node can only be set active, if a parent is active
      for (final ProgressNode parent : this.parents) {
        if (parent.isActive()) {
          State.set(this.id, true);
          return;
        }
      }
      // no parent is active: error
      // TODO
    }
  }
  
  boolean isActive() {
    if (!hasBeenReached()) {
      return false;
    }
    for (final ProgressNode child : this.children) {
      if (child.hasBeenReached()) {
        return false;
      }
    }
    return true;
  }
  
  @Override
  public String toString() {
    return this.id.name() + "[" + this.children.toString() + "]";
  }
  
  public ProgressNode getActiveNode() {
    if (isActive()) {
      return this;
    }
    for (final ProgressNode child : this.children) {
      if (child.hasBeenReached()) {
        return child.getActiveNode();
      }
    }
    // TODO error
    return null;
  }
  
  public PG getId() {
    return this.id;
  }
  
  public void draw(final Graphics2D g2d, final int width, final int height, final int[] widthsPerLevels,
      final int totalGraphDepth, final int nodeSize) {
    for (final ProgressNode child : this.children) {
      drawEdge(g2d, width, height, widthsPerLevels, totalGraphDepth, this, child);
      child.draw(g2d, width, height, widthsPerLevels, totalGraphDepth, nodeSize);
    }
    drawThisNode(g2d, width, height, widthsPerLevels, totalGraphDepth, nodeSize);
  }
  
  private void drawThisNode(final Graphics2D g2d, final int width, final int height, final int[] widthsPerLevels,
      final int totalGraphDepth, final int size) {
    final Point position = determinePosition(width, height, widthsPerLevels, totalGraphDepth, this);
    final Color color = determineNodeColor(this);
    g2d.setColor(color);
    g2d.fillArc(position.x - size / 2, position.y - size / 2, size, size, 0, 360);
    if (hasBeenReached()) {
      final String name = getId().name();
      g2d.setColor(Color.BLACK);
      final FontMetrics fm = g2d.getFontMetrics();
      final Rectangle2D bounds = fm.getStringBounds(name, g2d);
      g2d.drawString(name, position.x - (int) (bounds.getWidth() / 2), position.y + (int) (bounds.getHeight() * 0.3f));
    }
  }
  
  private Color determineNodeColor(final ProgressNode node) {
    if (node.isActive()) {
      return Color.RED;
    }
    if (node.hasBeenReached()) {
      return Color.BLUE;
    }
    if (node.hasActiveParent()) {
      return Color.CYAN;
    }
    return Color.BLACK;
  }
  
  private boolean hasActiveParent() {
    for (final ProgressNode parent : this.parents) {
      if (parent.isActive()) {
        return true;
      }
    }
    return false;
  }
  
  private static void drawEdge(final Graphics2D g2d, final int width, final int height, final int[] widthsPerLevels,
      final int totalGraphDepth, final ProgressNode parent, final ProgressNode child) {
    final Point parentPosition = determinePosition(width, height, widthsPerLevels, totalGraphDepth, parent);
    final Point childPosition = determinePosition(width, height, widthsPerLevels, totalGraphDepth, child);
    final Color color = determineEdgeColor(parent, child);
    g2d.setColor(color);
    g2d.drawLine(parentPosition.x, parentPosition.y, childPosition.x, childPosition.y);
  }
  
  private static Color determineEdgeColor(final ProgressNode parent, final ProgressNode child) {
    if (parent.hasBeenReached() && child.hasBeenReached()) {
      return Color.BLUE;
    }
    if (parent.isActive()) {
      return Color.CYAN;
    } else {
      return Color.BLACK;
    }
  }
  
  private static Point determinePosition(final int width, final int height, final int[] widthsPerLevels,
      final int totalGraphDepth, final ProgressNode node) {
    final int cellWidth = width / widthsPerLevels[node.getLevel()];
    final int cellHeight = height / totalGraphDepth;
    
    final int x = (int) ((node.getWidthInGraph() - 0.5) * cellWidth); // offset
    
    final int level = totalGraphDepth - node.getLevel();
    final int y = (int) (cellHeight * (level - 0.5));
    
    return new Point(x, y);
  }
  
  public boolean isRoot() {
    return this.parents.isEmpty();
  }
  
  public boolean hasChildren() {
    return !this.children.isEmpty();
  }
}
