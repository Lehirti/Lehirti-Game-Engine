package org.lehirti.engine.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextArea extends JTextArea implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TextArea.class);
  
  private final List<TextWrapper> allTexts = new ArrayList<TextWrapper>(25);
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  private int currentPage = 0;
  private int totalNumberOfPages = 0;
  
  public TextArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    
    getCaret().setVisible(false);
    setLineWrap(true);
    setWrapStyleWord(true);
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
  }
  
  public void addText(final TextWrapper text) {
    if (text != null) {
      this.allTexts.add(text);
    }
    this.currentPage = 0;
    refresh();
  }
  
  public void refresh() {
    final StringBuilder sb = new StringBuilder();
    for (final TextWrapper text : this.allTexts) {
      sb.append(text.getValue());
    }
    adjustFontToWindowSize();
    setText(sb.toString());
  }
  
  @Override
  public void setText(final String t) {
    int nrPages = 1;
    boolean textDoesNotFit;
    do {
      textDoesNotFit = false;
      final String[] pages = splitIntoPages(t, nrPages);
      for (final String page : pages) {
        if (!pageFits(page)) {
          textDoesNotFit = true;
          nrPages++;
          break;
        }
      }
      if (!textDoesNotFit) {
        if (pages.length <= this.currentPage) {
          this.currentPage = 0;
        }
        this.totalNumberOfPages = pages.length;
        super.setText(pages[this.currentPage]);
      }
      
    } while (textDoesNotFit);
  }
  
  private boolean pageFits(final String page) {
    super.setText(page);
    try {
      final Rectangle positionOfLastCharacter = modelToView(page.length());
      if (positionOfLastCharacter != null && positionOfLastCharacter.getY() > getHeight()) {
        return false;
      } else {
        return true;
      }
    } catch (final BadLocationException ignore) {
      return true;
    }
  }
  
  private String[] splitIntoPages(final String t, final int nrPages) {
    final String[] pages = new String[nrPages];
    int beginOfNextPage = 0;
    
    for (int i = 0; i < nrPages; i++) {
      pages[i] = t.substring(beginOfNextPage, beginOfNextPage + t.length() / nrPages);
      beginOfNextPage += t.length() / nrPages;
    }
    
    return pages;
  }
  
  private void adjustFontToWindowSize() {
    final Font oldFont = getFont();
    final JRootPane rootPane = getRootPane();
    int fontSize = 12;
    if (rootPane != null) {
      fontSize = getRootPane().getWidth() / 72;
    }
    if (fontSize < 12) {
      fontSize = 12;
    }
    if (oldFont == null || oldFont.getSize() != fontSize) {
      final Font font = new Font("Verdana", Font.BOLD, fontSize);
      setFont(font);
    }
  }
  
  public List<TextWrapper> getAllTexts() {
    final List<TextWrapper> allTextsIncludeCascaded = new LinkedList<TextWrapper>();
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
