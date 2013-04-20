package lge.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import lge.gui.WindowLocation.WinLoc;
import lge.res.images.ImageKey;
import lge.res.images.ImageProxy.ProxyProps;
import lge.res.images.ImageWrapper;
import lge.state.DateTime.DayPhase;
import lge.util.PathFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageEditor.class);
  
  private static File CURRENT_DIRECTORY = new File(".");
  
  private static final class NumericalInputVerifier extends InputVerifier {
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
      } catch (final RuntimeException e) {
        // user typed non-numerical values in text field
      }
      ImageEditor.this.updateCanvasAndImageWrapper();
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
  JLabel rotationlabel = new JLabel("Degrees Rotation");
  
  JButton alignX = new JButton();
  JButton alignY = new JButton();
  JTextField posX = new JTextField();
  JTextField posY = new JTextField();
  JTextField scaleX = new JTextField();
  JTextField scaleY = new JTextField();
  JTextField rotation = new JTextField();
  
  JLabel selectedImageLabel = new JLabel("Image #");
  JButton selectedImage = new JButton();
  JLabel selectedAlternativeLabel = new JLabel("Alternative #");
  JButton selectedAlternative = new JButton();
  
  JLabel newAlternativeLabel = new JLabel("Alternative");
  final JComboBox<String> contentDir;
  JButton newAlternative = new JButton("Add");
  
  JLabel attributeLabel = new JLabel("Attribute");
  
  private final String[] dayPhases = new String[DayPhase.values().length + 1];
  {
    this.dayPhases[0] = "";
    int i = 1;
    for (final DayPhase phase : DayPhase.values()) {
      this.dayPhases[i++] = phase.name();
    }
  }
  
  JComboBox<String> attribute = new JComboBox<>(this.dayPhases);
  
  JLabel deleteLabel = new JLabel("Mark as");
  JButton delete = new JButton("Deleted");
  
  ImageArea imageArea = new ImageArea(16.0, 12.0, 12.0, 9.0);
  
  final ImageArea gameImageArea;
  final List<ImageWrapper> allImages;
  int selectedImageNr = -1;
  int selectedAlternativeNr = -1;
  
  public ImageEditor(final List<ImageWrapper> allImages, final ImageArea gameImageArea) {
    this.gameImageArea = gameImageArea;
    this.allImages = allImages;
    if (!this.allImages.isEmpty()) {
      this.selectedImageNr = 0;
      setImage();
    }
    
    this.contentDir = new JComboBox<>(PathFinder.getContentDirs());
    
    this.controls.setLayout(new GridLayout(13, 2));
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
    this.controls.add(this.rotationlabel);
    this.controls.add(this.rotation);
    this.rotation.setInputVerifier(new NumericalInputVerifier(this));
    this.rotation.getDocument().addDocumentListener(new NumericalInputChangeListener(this.rotation));
    this.controls.add(this.selectedImageLabel);
    this.controls.add(this.selectedImage);
    this.controls.add(this.selectedAlternativeLabel);
    this.controls.add(this.selectedAlternative);
    this.controls.add(new JLabel());
    this.controls.add(this.contentDir);
    this.controls.add(this.newAlternativeLabel);
    this.controls.add(this.newAlternative);
    this.controls.add(this.attributeLabel);
    this.controls.add(this.attribute);
    this.controls.add(this.deleteLabel);
    this.controls.add(this.delete);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.controls, BorderLayout.EAST);
    this.all.add(this.imageArea, BorderLayout.CENTER);
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    this.alignX.addActionListener(this);
    this.alignY.addActionListener(this);
    this.rotation.addActionListener(this);
    this.selectedImage.addActionListener(this);
    this.selectedAlternative.addActionListener(this);
    this.newAlternative.addActionListener(this);
    this.attribute.addActionListener(this);
    this.delete.addActionListener(this);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setTitle();
    
    pack();
    
    setVisible(true);
    
    final WinLoc winLoc = WindowLocation.getLocationFor(ImageEditor.class.getSimpleName());
    if (!winLoc.isMax) {
      setLocation(winLoc.x, winLoc.y);
      setSize(winLoc.width, winLoc.height);
    } else {
      setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    addWindowListener(new WindowCloseListener(this, ImageEditor.class.getSimpleName()));
  }
  
  private void setTitle() {
    if (this.selectedImageNr != -1) {
      final ImageKey key = this.allImages.get(this.selectedImageNr).getKey();
      setTitle(key.getClass().getName() + "." + key.name());
    } else {
      setTitle("No images!");
    }
  }
  
  private void setImage() {
    LOGGER.debug("setImage({});", Integer.valueOf(this.selectedImageNr));
    
    setTitle();
    
    this.alignX.setText("");
    this.alignY.setText("");
    this.posX.setText("");
    this.posX.setEnabled(true);
    this.posY.setText("");
    this.posY.setEnabled(true);
    this.scaleX.setText("");
    this.scaleY.setText("");
    this.rotation.setText("");
    
    final ImageWrapper imageWrapper = this.allImages.get(this.selectedImageNr);
    this.imageArea.setImage(imageWrapper);
    this.selectedImage.setText(this.allImages.get(this.selectedImageNr).toButtonString());
    this.selectedImage.setToolTipText(this.allImages.get(this.selectedImageNr).toButtonString());
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
      case ATTRIBUTE:
        this.attribute.setSelectedItem(value);
      }
    }
    if (this.selectedAlternativeNr == 0) {
      this.attribute.setEnabled(false);
      this.attribute.setSelectedIndex(0);
    } else {
      this.attribute.setEnabled(true);
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
    setImage();
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
    } else if (e.getSource() == this.rotation) {
      updateCanvasAndImageWrapper();
    } else if (e.getSource() == this.selectedImage) {
      LOGGER.debug("selectNextImage()");
      selectNextImage();
    } else if (e.getSource() == this.selectedAlternative) {
      LOGGER.debug("selectNextAlternative()");
      this.allImages.get(this.selectedImageNr).pinNextImage();
      setImage();
    } else if (e.getSource() == this.newAlternative) {
      LOGGER.debug("addAlternative()");
      final JFileChooser fc = new JFileChooser(CURRENT_DIRECTORY);
      final int returnVal = fc.showOpenDialog(this);
      CURRENT_DIRECTORY = fc.getCurrentDirectory();
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        LOGGER.debug("file selected");
        final File file = fc.getSelectedFile();
        final String contentDirectory = (String) this.contentDir.getSelectedItem();
        this.allImages.get(this.selectedImageNr).addAlternativeImage(file, contentDirectory);
        this.selectedAlternativeNr = this.allImages.get(this.selectedImageNr).getCurrentImageNr();
        this.selectedAlternative.setText(String.valueOf(this.selectedAlternativeNr));
        setImage();
      } else {
        LOGGER.debug("aborted");
      }
      updateCanvasAndImageWrapper();
    } else if (e.getSource() == this.delete) {
      LOGGER.debug("delete()");
      final ImageWrapper imageWrapper = this.allImages.get(this.selectedImageNr);
      imageWrapper.removeAlternativeImage(this.selectedAlternativeNr);
      repaint();
      this.gameImageArea.repaint();
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
    final String rotationString = this.rotation.getText();
    if (!rotationString.equals("")) {
      placement.put(ProxyProps.ROTATION.name(), rotationString);
    }
    final String attributeString = (String) this.attribute.getSelectedItem();
    if (!attributeString.equals("")) {
      placement.put(ProxyProps.ATTRIBUTE.name(), attributeString);
    }
    this.allImages.get(this.selectedImageNr).setPlacement(placement);
    repaint();
    this.gameImageArea.repaint();
  }
}
