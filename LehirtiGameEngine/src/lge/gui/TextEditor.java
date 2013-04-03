package lge.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import lge.gui.WindowLocation.WinLoc;
import lge.res.text.TextKey;
import lge.res.text.TextParameterResolutionException;
import lge.res.text.TextWrapper;
import lge.util.PathFinder;

public class TextEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  JPanel all = new JPanel();
  
  JPanel controls = new JPanel();
  final JComboBox<String> contentDir;
  JButton prev = new JButton("Previous");
  JButton next = new JButton("Next");
  
  JButton save = new JButton("Save");
  
  JPanel textAreas = new JPanel();
  
  final TextArea gameTextArea;
  final OptionArea gameOptionArea;
  final List<TextWrapper> allTexts;
  final List<TextWrapper> allOptions;
  int selectedTextNr = -1;
  int selectedOptionNr = -1;
  
  public TextEditor(final TextArea gameTextArea, final OptionArea gameOptionArea, final List<TextWrapper> textAreaTexts) {
    this.gameTextArea = gameTextArea;
    this.gameOptionArea = gameOptionArea;
    this.allTexts = textAreaTexts;
    this.allOptions = gameOptionArea.getAllOptions();
    if (!this.allTexts.isEmpty()) {
      this.selectedTextNr = 0;
      setTexts(this.allTexts.get(0).getRawValues());
    } else if (!this.allOptions.isEmpty()) {
      this.selectedOptionNr = 0;
      setTexts(this.allOptions.get(0).getRawValues());
    }
    
    this.textAreas.setLayout(new BoxLayout(this.textAreas, BoxLayout.Y_AXIS));
    
    this.contentDir = new JComboBox<>(PathFinder.getContentDirs());
    
    this.controls.setLayout(new GridLayout(4, 1));
    this.controls.setPreferredSize(new Dimension(300, 800));
    this.controls.add(this.prev);
    this.controls.add(this.next);
    this.controls.add(this.contentDir);
    this.controls.add(this.save);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.controls, BorderLayout.EAST);
    this.all.add(this.textAreas, BorderLayout.CENTER);
    
    setTitle();
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    this.prev.addActionListener(this);
    this.next.addActionListener(this);
    this.save.addActionListener(this);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    pack();
    
    setVisible(true);
    
    final WinLoc winLoc = WindowLocation.getLocationFor(TextEditor.class.getSimpleName());
    if (!winLoc.isMax) {
      setLocation(winLoc.x, winLoc.y);
      setSize(winLoc.width, winLoc.height);
    } else {
      setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    addWindowListener(new WindowCloseListener(this, TextEditor.class.getSimpleName()));
  }
  
  private void setTitle() {
    TextKey textKey = null;
    if (this.selectedOptionNr != -1) {
      textKey = this.allOptions.get(this.selectedOptionNr).getTextKey();
    } else if (this.selectedTextNr != -1) {
      textKey = this.allTexts.get(this.selectedTextNr).getTextKey();
    }
    
    if (textKey != null) {
      setTitle(textKey.getClass().getName() + "." + textKey.name());
    } else {
      setTitle("No texts!");
    }
  }
  
  private void selectPrev() {
    if (this.selectedTextNr != -1) {
      if (!selectPrevText()) {
        if (!selectLastOption()) {
          selectLastText();
        }
      }
    } else if (this.selectedOptionNr != -1) {
      if (!selectPrevOption()) {
        if (!selectLastText()) {
          selectLastOption();
        }
      }
    }
    setTitle();
  }
  
  private void selectNext() {
    if (this.selectedTextNr != -1) {
      if (!selectNextText()) {
        if (!selectFirstOption()) {
          selectFirstText();
        }
      }
    } else if (this.selectedOptionNr != -1) {
      if (!selectNextOption()) {
        if (!selectFirstText()) {
          selectFirstOption();
        }
      }
    }
    setTitle();
  }
  
  private boolean selectLastOption() {
    if (this.allOptions.isEmpty()) {
      return false;
    }
    this.selectedOptionNr = this.allOptions.size() - 1;
    setTexts(this.allOptions.get(this.selectedOptionNr).getRawValues());
    return true;
  }
  
  private boolean selectFirstOption() {
    if (this.allOptions.isEmpty()) {
      return false;
    }
    this.selectedOptionNr = 0;
    setTexts(this.allOptions.get(this.selectedOptionNr).getRawValues());
    return true;
  }
  
  private boolean selectLastText() {
    if (this.allTexts.isEmpty()) {
      return false;
    }
    this.selectedTextNr = this.allTexts.size() - 1;
    setTexts(this.allTexts.get(this.selectedTextNr).getRawValues());
    return true;
  }
  
  private boolean selectFirstText() {
    if (this.allTexts.isEmpty()) {
      return false;
    }
    this.selectedTextNr = 0;
    setTexts(this.allTexts.get(this.selectedTextNr).getRawValues());
    return true;
  }
  
  private boolean selectPrevOption() {
    this.selectedOptionNr--;
    if (this.selectedOptionNr == -1) {
      return false;
    }
    setTexts(this.allOptions.get(this.selectedOptionNr).getRawValues());
    return true;
  }
  
  private boolean selectNextOption() {
    this.selectedOptionNr++;
    if (this.selectedOptionNr >= this.allOptions.size()) {
      this.selectedOptionNr = -1;
      return false;
    }
    setTexts(this.allOptions.get(this.selectedOptionNr).getRawValues());
    return true;
  }
  
  private boolean selectPrevText() {
    this.selectedTextNr--;
    if (this.selectedTextNr == -1) {
      return false;
    }
    setTexts(this.allTexts.get(this.selectedTextNr).getRawValues());
    return true;
  }
  
  private boolean selectNextText() {
    this.selectedTextNr++;
    if (this.selectedTextNr >= this.allTexts.size()) {
      this.selectedTextNr = -1;
      return false;
    }
    setTexts(this.allTexts.get(this.selectedTextNr).getRawValues());
    return true;
  }
  
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() == this.prev) {
      selectPrev();
    } else if (e.getSource() == this.next) {
      selectNext();
    } else if (e.getSource() == this.save) {
      final String contentDirectory = (String) this.contentDir.getSelectedItem();
      TextWrapper textWrapper = null;
      if (this.selectedTextNr != -1) {
        textWrapper = this.allTexts.get(this.selectedTextNr);
      } else if (this.selectedOptionNr != -1) {
        textWrapper = this.allOptions.get(this.selectedOptionNr);
      }
      if (textWrapper != null) {
        try {
          textWrapper.setRawValues(getTexts(), contentDirectory);
        } catch (final TextParameterResolutionException e1) {
          new Notification(this, "Cannot save texts due to error in parameter: " + e1.getMessage());
        }
      }
    }
    this.gameTextArea.refresh();
    this.gameOptionArea.repaint();
  }
  
  private String[] getTexts() {
    final List<String> texts = new LinkedList<>();
    
    final Component[] components = this.textAreas.getComponents();
    for (final Component area : components) {
      final String text = ((JTextArea) area).getText();
      if (!text.isEmpty()) {
        texts.add(text);
      }
    }
    if (texts.isEmpty()) {
      final String[] ret = new String[] { "" };
      return ret;
    }
    return texts.toArray(new String[texts.size()]);
  }
  
  private void setTexts(final String[] texts) {
    this.textAreas.removeAll();
    for (final String text : texts) {
      if (text != null) {
        final JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new LineBorder(Color.BLACK));
        this.textAreas.add(textArea);
        textArea.setText(text);
      }
    }
    final JTextArea textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setBorder(new LineBorder(Color.BLACK));
    this.textAreas.add(textArea);
  }
}
