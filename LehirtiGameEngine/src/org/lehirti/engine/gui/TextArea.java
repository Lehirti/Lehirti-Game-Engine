package org.lehirti.engine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.CommonImage;
import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextArea extends JTextPane implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TextArea.class);
  
  private final ConcurrentLinkedQueue<TextWrapper> allTexts = new ConcurrentLinkedQueue<>();
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  private int currentPage = 0;
  private int totalNumberOfPages = 0;
  
  private boolean repaintIfNeeded = false;
  
  public TextArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    
    getCaret().setVisible(false);
    // setLineWrap(true);
    // setWrapStyleWord(true);
    setFocusable(false);
    setOpaque(false);
  }
  
  @Override
  public void repaint() {
    if (getCaret() != null) {
      getCaret().setVisible(false);
    }
    adjustFontToWindowSize();
    super.repaint();
  }
  
  @Override
  public void paintComponent(final Graphics g) {
    final Graphics2D g2 = (Graphics2D) g;
    final BufferedImage bufferedImage = ResourceCache.get(CommonImage.TEXT_AREA_BACKGROUND).getImage();
    final Rectangle rect = new Rectangle(0, 0, bufferedImage.getWidth(null), bufferedImage.getHeight(null));
    final TexturePaint texturePaint = new TexturePaint(bufferedImage, rect);
    g2.setPaint(texturePaint);
    g.fillRect(0, 0, getWidth(), getHeight());
    super.paintComponent(g);
    if (this.totalNumberOfPages > 1) {
      g.setColor(Color.BLACK);
      final String pageXofY = (this.currentPage + 1) + "/" + this.totalNumberOfPages;
      g.drawString(pageXofY, (30 * getWidth()) / 31 - getFontMetrics(getFont()).stringWidth(pageXofY), getHeight()
          - (int) (0.5f * getFont().getSize2D()));
    }
  }
  
  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getPreferredSize() {
    final Dimension size = getParent().getSize();
    double width = size.width;
    double height = size.height;
    if (width < 640) {
      width = 640;
    }
    if (height < 640) {
      height = 640;
    }
    
    final double baseSizeX = width / this.screenX;
    final double baseSizeY = height / this.screenY;
    final double baseSize = baseSizeX < baseSizeY ? baseSizeX : baseSizeY;
    
    width = baseSize * this.sizeX;
    height = baseSize * this.sizeY;
    
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("preferredSize: " + width + " " + height);
    }
    return new Dimension((int) width, (int) height);
  }
  
  public void setText(final TextWrapper text) {
    this.allTexts.clear();
    addText(text);
    this.repaintIfNeeded = true;
  }
  
  public void addText(final TextWrapper text) {
    if (text != null) {
      this.allTexts.add(text);
    }
    this.currentPage = 0;
    this.repaintIfNeeded = true;
  }
  
  public void repaintIfNeeded() {
    if (this.repaintIfNeeded) {
      this.repaintIfNeeded = false;
      try {
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            refresh();
          }
        });
      } catch (final InterruptedException e) {
        LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
        throw new ThreadDeath();
      } catch (final InvocationTargetException e) {
        LOGGER.error("InvocationTargetException trying to set text to text area", e);
      }
    }
  }
  
  void refresh() {
    final StringBuilder sb = new StringBuilder();
    for (final TextWrapper text : this.allTexts) {
      sb.append(text.getValue());
    }
    adjustFontToWindowSize();
    setText(sb.toString());
  }
  
  @Override
  public void setText(final String t) {
    if (pageFits(t)) {
      this.totalNumberOfPages = 1;
      this.currentPage = 0;
      super.setText(t);
      return;
    }
    final List<String> pages = new LinkedList<>();
    final String[] paragraphs = t.split("\\r?\\n|\\r");
    for (int i = 0; i < paragraphs.length;) {
      String page = paragraphs[i++];
      if (!pageFits(page)) {
        // split single paragraph, if it does not fit on one page
        splitParagraph(pages, page);
      } else {
        // add as many paragraphs to one page, as fit
        String oldPage = page;
        while (i < paragraphs.length) {
          page = page + "\n" + paragraphs[i++];
          if (!pageFits(page)) {
            i--;
            break;
          }
          oldPage = page;
        }
        pages.add(oldPage.trim());
      }
    }
    this.totalNumberOfPages = pages.size();
    if (this.currentPage >= this.totalNumberOfPages) {
      this.currentPage = 0;
    }
    super.setText(pages.get(this.currentPage));
  }
  
  // this paragraph is too long and does not fit onto one page; split at sentence level
  private void splitParagraph(final List<String> pages, final String paragraph) {
    String oldPage = "";
    String page = "";
    final String[] sentences = paragraph.split("\\.");
    for (final String sentence : sentences) {
      oldPage = page;
      page += sentence + ".";
      if (!pageFits(page)) {
        pages.add(oldPage.trim());
        page = sentence + ".";
      }
    }
    pages.add(page.trim());
  }
  
  private boolean pageFits(final String page) {
    super.setText(page);
    try {
      final Rectangle positionOfLastCharacter = modelToView(page.length());
      if (positionOfLastCharacter != null
          && positionOfLastCharacter.getY() > getHeight() - 2.5f * getFont().getSize2D()) {
        return false;
      } else {
        return true;
      }
    } catch (final BadLocationException ignore) {
      return true;
    }
  }
  
  private void adjustFontToWindowSize() {
    final Font oldFont = getFont();
    if (oldFont != null) {
      final Font newFont = getScaledFont();
      if (oldFont.getSize() != newFont.getSize()) {
        setFont(newFont);
      }
    }
  }
  
  public Font getScaledFont() {
    int fontSize = getWidth() / 18;
    if (fontSize < 12) {
      fontSize = 12;
    }
    return new Font("Verdana", Font.BOLD, fontSize);
  }
  
  public List<TextWrapper> getAllTexts() {
    final List<TextWrapper> allTextsIncludeCascaded = new LinkedList<>();
    for (final TextWrapper txtWrp : this.allTexts) {
      allTextsIncludeCascaded.addAll(txtWrp.getAllTexts());
    }
    return allTextsIncludeCascaded;
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    this.allTexts.clear();
    final int nrOfTexts = in.readInt();
    for (int i = 0; i < nrOfTexts; i++) {
      this.allTexts.add((TextWrapper) in.readObject());
    }
    refresh();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.allTexts.size());
    for (final TextWrapper text : this.allTexts) {
      out.writeObject(text);
    }
  }
  
  public void cycleToNextPage() {
    this.currentPage++;
    refresh();
  }
}
