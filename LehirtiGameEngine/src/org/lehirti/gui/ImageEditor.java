package org.lehirti.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.lehirti.res.images.ImageWrapper;
import org.lehirti.res.images.ImageProxy.ProxyProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageEditor.class);
  
  private final class NumericalInputVerifier extends InputVerifier {
    private final ImageEditor imgEditor;
    
    public NumericalInputVerifier(final ImageEditor imgEditor) {
      this.imgEditor = imgEditor;
    }
    
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
      } finally {
        this.imgEditor.updateCanvasAndImageWrapper();
      }
    }
    
  }
  
  private final class NumericalInputChangeListener implements DocumentListener {
    JTextField textField;
    
    public NumericalInputChangeListener(final JTextField field) {
      this.textField = field;
    }
    
    @Override
    public void changedUpdate(final DocumentEvent e) {
      update();
    }
    
    @Override
    public void insertUpdate(final DocumentEvent e) {
      update();
    }
    
    @Override
    public void removeUpdate(final DocumentEvent e) {
      update();
    }
    
    private final void update() {
      try {
        if (this.textField.getText().trim().length() == 0) {
          this.textField.setText("");
        }
        Double.valueOf(this.textField.getText()).doubleValue();
        ImageEditor.this.updateCanvasAndImageWrapper();
      } catch (final RuntimeException e) {
        // user typed non-numerical values in text field
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
  
  JLabel newAlternativeLabel = new JLabel("Alternative");
  JButton newAlternative = new JButton("Add");
  
  ImageArea imageArea = new ImageArea();
  
  final ImageArea gameImageArea;
  final List<ImageWrapper> allImages;
  int selectedImageNr = -1;
  int selectedAlternativeNr = -1;
  
  public ImageEditor(final List<ImageWrapper> allImages, final ImageArea gameImageArea) {
    this.gameImageArea = gameImageArea;
    this.allImages = allImages;
    if (!this.allImages.isEmpty()) {
      this.selectedImageNr = 0;
      setImage(0);
    }
    
    this.controls.setLayout(new GridLayout(9, 2));
    this.controls.setPreferredSize(new Dimension(300, 800));
    this.controls.add(this.alignXlabel);
    this.controls.add(this.alignX);
    this.controls.add(this.alignYlabel);
    this.controls.add(this.alignY);
    this.controls.add(this.posXlabel);
    this.controls.add(this.posX);
    this.posX.setInputVerifier(new NumericalInputVerifier(this));
    this.posX.getDocument().addDocumentListener(new NumericalInputChangeListener(this.posX));
    this.controls.add(this.posYlabel);
    this.controls.add(this.posY);
    this.posY.setInputVerifier(new NumericalInputVerifier(this));
    this.posY.getDocument().addDocumentListener(new NumericalInputChangeListener(this.posY));
    this.controls.add(this.scaleXlabel);
    this.controls.add(this.scaleX);
    this.scaleX.setInputVerifier(new NumericalInputVerifier(this));
    this.scaleX.getDocument().addDocumentListener(new NumericalInputChangeListener(this.scaleX));
    this.controls.add(this.scaleYlabel);
    this.controls.add(this.scaleY);
    this.scaleY.setInputVerifier(new NumericalInputVerifier(this));
    this.scaleY.getDocument().addDocumentListener(new NumericalInputChangeListener(this.scaleY));
    this.controls.add(this.selectedImageLabel);
    this.controls.add(this.selectedImage);
    this.controls.add(this.selectedAlternativeLabel);
    this.controls.add(this.selectedAlternative);
    this.controls.add(this.newAlternativeLabel);
    this.controls.add(this.newAlternative);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.controls, BorderLayout.EAST);
    this.all.add(this.imageArea, BorderLayout.CENTER);
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    this.alignX.addActionListener(this);
    this.alignY.addActionListener(this);
    this.selectedImage.addActionListener(this);
    this.selectedAlternative.addActionListener(this);
    this.newAlternative.addActionListener(this);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setSize(1200, 800);
    
    setVisible(true);
  }
  
  private void setImage(final int nr) {
    LOGGER.debug("setImage({});", nr);
    
    this.alignX.setText("");
    this.alignY.setText("");
    this.posX.setText("");
    this.posX.setEnabled(true);
    this.posY.setText("");
    this.posY.setEnabled(true);
    this.scaleX.setText("");
    this.scaleY.setText("");
    
    final ImageWrapper imageWrapper = this.allImages.get(nr);
    this.imageArea.setImage(imageWrapper);
    this.selectedImage.setText(this.allImages.get(nr).toButtonString());
    this.selectedImage.setToolTipText(this.allImages.get(nr).toButtonString());
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
        this.posX.setEnabled(!value.equals("CENTER"));
        break;
      case ALIGN_Y:
        this.alignY.setText(value);
        this.posY.setEnabled(!value.equals("CENTER"));
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
    this.gameImageArea.repaint();
  }
  
  private void selectNextImage() {
    if (this.allImages.isEmpty()) {
      this.selectedImage.setText("NONE AVAILABLE");
      return;
    }
    this.selectedImageNr++;
    if (this.selectedImageNr >= this.allImages.size()) {
      this.selectedImageNr = 0;
    }
    setImage(this.selectedImageNr);
  }
  
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() == this.alignX) {
      final String currentText = this.alignX.getText();
      if (currentText == null || currentText.equals("")) {
        this.alignX.setText("LEFT");
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("LEFT")) {
        this.alignX.setText("CENTER");
        this.posX.setEnabled(false);
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("CENTER")) {
        this.alignX.setText("RIGHT");
        this.posX.setEnabled(true);
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("RIGHT")) {
        this.alignX.setText("LEFT");
        updateCanvasAndImageWrapper();
      }
    } else if (e.getSource() == this.alignY) {
      final String currentText = this.alignY.getText();
      if (currentText == null || currentText.equals("")) {
        this.alignY.setText("TOP");
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("TOP")) {
        this.alignY.setText("CENTER");
        this.posY.setEnabled(false);
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("CENTER")) {
        this.alignY.setText("BOTTOM");
        this.posY.setEnabled(true);
        updateCanvasAndImageWrapper();
      } else if (currentText.equals("BOTTOM")) {
        this.alignY.setText("TOP");
        updateCanvasAndImageWrapper();
      }
    } else if (e.getSource() == this.selectedImage) {
      LOGGER.debug("selectNextImage()");
      selectNextImage();
    } else if (e.getSource() == this.selectedAlternative) {
      LOGGER.debug("selectNextAlternative()");
      this.allImages.get(this.selectedImageNr).pinNextImage();
      setImage(this.selectedImageNr);
    } else if (e.getSource() == this.newAlternative) {
      LOGGER.debug("addAlternative()");
      final JFileChooser fc = new JFileChooser();
      final int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        LOGGER.debug("file selected");
        final File file = fc.getSelectedFile();
        this.allImages.get(this.selectedImageNr).addAlternativeImage(file);
        this.selectedAlternativeNr = this.allImages.get(this.selectedImageNr).getCurrentImageNr();
        this.selectedAlternative.setText(String.valueOf(this.selectedAlternativeNr));
        setImage(this.selectedImageNr);
      } else {
        LOGGER.debug("aborted");
      }
      updateCanvasAndImageWrapper();
    } else {
      updateCanvasAndImageWrapper();
    }
  }
  
  private void updateCanvasAndImageWrapper() {
    LOGGER.debug("updateCanvasAndImageWrapper");
    final Properties placement = new Properties();
    final String alignXstring = this.alignX.getText();
    if (!alignXstring.equals("")) {
      placement.put(ProxyProps.ALIGN_X.name(), alignXstring);
    }
    final String alignYstring = this.alignY.getText();
    if (!alignYstring.equals("")) {
      placement.put(ProxyProps.ALIGN_Y.name(), alignYstring);
    }
    final String posXstring = this.posX.getText();
    if (!posXstring.equals("")) {
      placement.put(ProxyProps.POS_X.name(), posXstring);
    }
    final String posYstring = this.posY.getText();
    if (!posYstring.equals("")) {
      placement.put(ProxyProps.POS_Y.name(), posYstring);
    }
    final String scaleXstring = this.scaleX.getText();
    if (!scaleXstring.equals("")) {
      placement.put(ProxyProps.SCALE_X.name(), scaleXstring);
    }
    final String scaleYstring = this.scaleY.getText();
    if (!scaleYstring.equals("")) {
      placement.put(ProxyProps.SCALE_Y.name(), scaleYstring);
    }
    this.allImages.get(this.selectedImageNr).setPlacement(placement);
    repaint();
    this.gameImageArea.repaint();
  }
}
