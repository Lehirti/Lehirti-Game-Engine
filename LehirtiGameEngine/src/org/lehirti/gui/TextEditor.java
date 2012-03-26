package org.lehirti.gui;

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

import org.lehirti.res.text.TextWrapper;
import org.lehirti.util.PathFinder;

public class TextEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  JPanel all = new JPanel();
  
  JPanel controls = new JPanel();
  final JComboBox contentDir;
  JButton next = new JButton("Next");
  
  JButton save = new JButton("Save");
  
  JTextArea textArea = new JTextArea();
  
  final TextArea gameTextArea;
  final List<TextWrapper> allTexts;
  int selectedTextNr = -1;
  
  public TextEditor(final TextArea gameTextArea) {
    this.gameTextArea = gameTextArea;
    this.allTexts = gameTextArea.getAllTexts();
    if (!this.allTexts.isEmpty()) {
      this.selectedTextNr = 0;
      this.textArea.setText(this.allTexts.get(0).getRawValue());
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
  
  private void selectNextText() {
    if (this.allTexts.isEmpty()) {
      setTitle("No Text available");
      return;
    }
    this.selectedTextNr++;
    if (this.selectedTextNr >= this.allTexts.size()) {
      this.selectedTextNr = 0;
    }
    this.textArea.setText(this.allTexts.get(this.selectedTextNr).getRawValue());
  }
  
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() == this.next) {
      selectNextText();
    } else if (e.getSource() == this.save) {
      final String contentDir = (String) this.contentDir.getSelectedItem();
      this.allTexts.get(this.selectedTextNr).setValue(this.textArea.getText(), contentDir); // TODO selected content dir
    }
    this.gameTextArea.refresh();
  }
}
