package org.lehirti.engine.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.util.PathFinder;

public class TextEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  JPanel all = new JPanel();
  
  JPanel controls = new JPanel();
  final JComboBox contentDir;
  JButton next = new JButton("Next");
  
  JButton save = new JButton("Save");
  
  JTextArea textArea = new JTextArea();
  
  final TextArea gameTextArea;
  final OptionArea gameOptionArea;
  final List<TextWrapper> allTexts;
  final List<TextWrapper> allOptions;
  int selectedTextNr = -1;
  int selectedOptionNr = -1;
  
  public TextEditor(final TextArea gameTextArea, final OptionArea gameOptionArea) {
    this.gameTextArea = gameTextArea;
    this.gameOptionArea = gameOptionArea;
    this.allTexts = gameTextArea.getAllTexts();
    this.allOptions = gameOptionArea.getAllOptions();
    if (!this.allTexts.isEmpty()) {
      this.selectedTextNr = 0;
      this.textArea.setText(this.allTexts.get(0).getRawValue());
    } else if (!this.allOptions.isEmpty()) {
      this.selectedOptionNr = 0;
      this.textArea.setText(this.allOptions.get(0).getRawValue());
    }
    this.textArea.setLineWrap(true);
    this.textArea.setWrapStyleWord(true);
    
    this.contentDir = new JComboBox(PathFinder.getContentDirs());
    
    this.controls.setLayout(new GridLayout(3, 1));
    this.controls.setPreferredSize(new Dimension(300, 800));
    this.controls.add(this.next);
    this.controls.add(this.contentDir);
    this.controls.add(this.save);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.controls, BorderLayout.EAST);
    this.all.add(this.textArea, BorderLayout.CENTER);
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    this.next.addActionListener(this);
    this.save.addActionListener(this);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setSize(1200, 800);
    
    setVisible(true);
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
    } else {
      if (this.allTexts.isEmpty()) {
        setTitle("No Text available");
        return;
      }
    }
  }
  
  private boolean selectFirstOption() {
    if (this.allOptions.isEmpty()) {
      return false;
    }
    this.selectedOptionNr = 0;
    this.textArea.setText(this.allOptions.get(this.selectedOptionNr).getRawValue());
    return true;
  }
  
  private boolean selectFirstText() {
    if (this.allTexts.isEmpty()) {
      return false;
    }
    this.selectedTextNr = 0;
    this.textArea.setText(this.allTexts.get(this.selectedTextNr).getRawValue());
    return true;
  }
  
  private boolean selectNextOption() {
    this.selectedOptionNr++;
    if (this.selectedOptionNr >= this.allOptions.size()) {
      this.selectedOptionNr = -1;
      return false;
    }
    this.textArea.setText(this.allOptions.get(this.selectedOptionNr).getRawValue());
    return true;
  }
  
  private boolean selectNextText() {
    this.selectedTextNr++;
    if (this.selectedTextNr >= this.allTexts.size()) {
      this.selectedTextNr = -1;
      return false;
    }
    this.textArea.setText(this.allTexts.get(this.selectedTextNr).getRawValue());
    return true;
  }
  
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() == this.next) {
      selectNext();
    } else if (e.getSource() == this.save) {
      final String contentDir = (String) this.contentDir.getSelectedItem();
      TextWrapper textWrapper = null;
      if (this.selectedTextNr != -1) {
        textWrapper = this.allTexts.get(this.selectedTextNr);
      } else if (this.selectedOptionNr != -1) {
        textWrapper = this.allOptions.get(this.selectedOptionNr);
      }
      if (textWrapper != null) {
        textWrapper.setValue(this.textArea.getText(), contentDir);
      }
    }
    this.gameTextArea.refresh();
    this.gameOptionArea.repaint();
  }
}
