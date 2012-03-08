package org.lehirti.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lehirti.res.images.ImageWrapper;
import org.lehirti.res.images.ImageProxy.ProxyProps;

public class ImageEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  private static final class NumericalInputVerifier extends InputVerifier {
    
    @Override
    public boolean verify(final JComponent input) {
      final JTextField textField = (JTextField) input;
      try {
        if (textField.getText().trim().length() == 0) {
          textField.setText("");
          return true;
        }
        Double.valueOf(textField.getText());
        return true;
      } catch (final RuntimeException e) {
        textField.setText("");
        return true;
      }
    }
    
  }
  
  JPanel all = new JPanel();
  
  JPanel controls = new JPanel();
  
  JLabel alignXlabel = new JLabel("Align X");
  JLabel alignYlabel = new JLabel("Align Y");
  JLabel posXlabel = new JLabel("Pos X");
  JLabel posYlabel = new JLabel("Pos Y");
  JLabel scaleXlabel = new JLabel("Scale X");
  JLabel scaleYlabel = new JLabel("Scale Y");
  
  JButton alignX = new JButton();
  JButton alignY = new JButton();
  JTextField posX = new JTextField();
  JTextField posY = new JTextField();
  JTextField scaleX = new JTextField();
  JTextField scaleY = new JTextField();
  
  JLabel selectedImageLabel = new JLabel("Image #");
  JButton selectedImage = new JButton();
  JLabel selectedAlternativeLabel = new JLabel("Alternative #");
  JButton selectedAlternative = new JButton();
  
  JCheckBox myCheckBox = new JCheckBox("Check");
  JCheckBox myCheckBox2 = new JCheckBox("Check 2");
  JCheckBox myCheckBox3 = new JCheckBox("Check 3");
  
  ImageArea imageArea = new ImageArea();
  
  final List<ImageWrapper> allImages;
  int selectedImageNr = -1;
  int selectedAlternativeNr = -1;
  
  public ImageEditor(final List<ImageWrapper> allImages) {
    this.allImages = allImages;
    if (!this.allImages.isEmpty()) {
      this.selectedImageNr = 0;
      setImage(0);
    }
    
    this.controls.setLayout(new GridLayout(8, 2));
    this.controls.setPreferredSize(new Dimension(200, 800));
    this.controls.add(this.alignXlabel);
    this.controls.add(this.alignX);
    this.controls.add(this.alignYlabel);
    this.controls.add(this.alignY);
    this.controls.add(this.posXlabel);
    this.controls.add(this.posX);
    this.posX.setInputVerifier(new NumericalInputVerifier());
    this.controls.add(this.posYlabel);
    this.controls.add(this.posY);
    this.posY.setInputVerifier(new NumericalInputVerifier());
    this.controls.add(this.scaleXlabel);
    this.controls.add(this.scaleX);
    this.scaleX.setInputVerifier(new NumericalInputVerifier());
    this.controls.add(this.scaleYlabel);
    this.controls.add(this.scaleY);
    this.scaleY.setInputVerifier(new NumericalInputVerifier());
    this.controls.add(this.selectedImageLabel);
    this.controls.add(this.selectedImage);
    this.controls.add(this.selectedAlternativeLabel);
    this.controls.add(this.selectedAlternative);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.controls, BorderLayout.EAST);
    this.all.add(this.imageArea, BorderLayout.CENTER);
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    this.alignX.addActionListener(this);
    this.alignY.addActionListener(this);
    this.selectedImage.addActionListener(this);
    this.selectedAlternative.addActionListener(this);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setSize(1200, 800);
    
    setVisible(true);
  }
  
  private void setImage(final int nr) {
    this.alignX.setText("");
    this.alignY.setText("");
    this.posX.setText("");
    this.posY.setText("");
    this.scaleX.setText("");
    this.scaleY.setText("");
    
    final ImageWrapper imageWrapper = this.allImages.get(nr);
    this.imageArea.setImage(imageWrapper);
    this.selectedImage.setText(String.valueOf(nr));
    this.selectedAlternativeNr = imageWrapper.getCurrentImageNr();
    this.selectedAlternative.setText(String.valueOf(this.selectedAlternativeNr));
    final Properties placement = imageWrapper.getPlacement();
    for (final Entry<Object, Object> entry : placement.entrySet()) {
      final String key = (String) entry.getKey();
      final String value = (String) entry.getValue();
      final ProxyProps props = ProxyProps.valueOf(key);
      switch (props) {
      case ALIGN_X:
        this.alignX.setText(value);
        break;
      case ALIGN_Y:
        this.alignY.setText(value);
        break;
      case POS_X:
        this.posX.setText(value);
        break;
      case POS_Y:
        this.posY.setText(value);
        break;
      case SCALE_X:
        this.scaleX.setText(value);
        break;
      case SCALE_Y:
        this.scaleY.setText(value);
        break;
      }
    }
    repaint();
  }
  
  private String selectNextImage() {
    if (this.allImages.isEmpty()) {
      return "NONE AVAILABLE";
    }
    this.selectedImageNr++;
    if (this.selectedImageNr >= this.allImages.size()) {
      this.selectedImageNr = -1;
      // TODO
      return "ALL";
    }
    setImage(this.selectedImageNr);
    return String.valueOf(this.selectedImageNr);
  }
  
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() == this.alignX) {
      final String currentText = this.alignX.getText();
      if (currentText == null || currentText.equals("")) {
        this.alignX.setText("LEFT");
      } else if (currentText.equals("LEFT")) {
        this.alignX.setText("CENTER");
        this.posX.setEnabled(false);
      } else if (currentText.equals("CENTER")) {
        this.alignX.setText("RIGHT");
        this.posX.setEnabled(true);
      } else if (currentText.equals("RIGHT")) {
        this.alignX.setText("LEFT");
      }
    } else if (e.getSource() == this.alignY) {
      final String currentText = this.alignY.getText();
      if (currentText == null || currentText.equals("")) {
        this.alignY.setText("TOP");
      } else if (currentText.equals("TOP")) {
        this.alignY.setText("CENTER");
        this.posY.setEnabled(false);
      } else if (currentText.equals("CENTER")) {
        this.alignY.setText("BOTTOM");
        this.posY.setEnabled(true);
      } else if (currentText.equals("BOTTOM")) {
        this.alignY.setText("TOP");
      }
    } else if (e.getSource() == this.selectedImage) {
      this.selectedImage.setText(selectNextImage());
    }
  }
}
