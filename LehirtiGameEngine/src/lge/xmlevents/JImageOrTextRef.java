package lge.xmlevents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public final class JImageOrTextRef extends JComboBox<String> implements EventValidateable {
  private static final long serialVersionUID = 1L;
  private static final Pattern IMAGE_AND_TEXT_REF_RESTRICTION_PATTERN = Pattern
      .compile("(([a-z][a-z_0-9]*\\.)+([A-Z][A-Za-z_0-9]+\\.){1,2})?[A-Z][A-Z_0-9]*");
  private static final Pattern IMAGE_AND_TEXT_INTERNAL_REF_RESTRICTION_PATTERN = Pattern.compile("[A-Z][A-Z_0-9]*");
  
  private boolean containsError = false;
  
  private final boolean mayBeEmpty;
  
  public JImageOrTextRef(final String[] possibleRefs, final boolean mayBeEmpty) {
    super(possibleRefs);
    
    this.mayBeEmpty = mayBeEmpty;
    
    setEditable(true);
    setPreferredSize(new Dimension(600, 20));
    
    final JTextComponent tc = (JTextComponent) getEditor().getEditorComponent();
    tc.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(final DocumentEvent e) {
        try {
          adjustBackgroundColor(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
      
      @Override
      public void removeUpdate(final DocumentEvent e) {
        try {
          adjustBackgroundColor(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
      
      @Override
      public void changedUpdate(final DocumentEvent e) {
        try {
          adjustBackgroundColor(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        adjustBackgroundColor(null);
      }
    });
    setSelectedItem("");
  }
  
  private void adjustBackgroundColor(final String text) {
    String value = text;
    boolean found = false;
    if (text == null) {
      value = (String) getSelectedItem();
      found = getSelectedIndex() != -1;
    } else {
      for (int i = 0; i < getModel().getSize(); i++) {
        if (getModel().getElementAt(i).equals(text)) {
          found = true;
          break;
        }
      }
    }
    if (!found) {
      if (this.mayBeEmpty && value.equals("")) {
        getEditor().getEditorComponent().setBackground(Color.WHITE);
        this.containsError = false;
        setToolTipText("This field is optional and my be left empty.");
      } else if (!IMAGE_AND_TEXT_REF_RESTRICTION_PATTERN.matcher(value).matches()) {
        getEditor().getEditorComponent().setBackground(Color.RED);
        this.containsError = true;
        setToolTipText("Field value \"" + value + "\" does not match required pattern \""
            + IMAGE_AND_TEXT_REF_RESTRICTION_PATTERN.pattern() + "\".");
      } else if (!IMAGE_AND_TEXT_INTERNAL_REF_RESTRICTION_PATTERN.matcher(value).matches()) {
        getEditor().getEditorComponent().setBackground(Color.RED);
        this.containsError = true;
        setToolTipText("This field makes an external references that does not exist.");
      } else {
        getEditor().getEditorComponent().setBackground(Color.WHITE);
        this.containsError = false;
        setToolTipText("A new reference with this name will be created inside the XML event.");
      }
    } else {
      getEditor().getEditorComponent().setBackground(Color.WHITE);
      this.containsError = false;
      setToolTipText("External reference exists, and can be used.");
    }
    Container ancestor = getParent();
    while (ancestor != null && !(ancestor instanceof EventPanel)) {
      ancestor = ancestor.getParent();
    }
    if (ancestor != null) {
      ((EventPanel) ancestor).updateStatus();
    }
  }
  
  @Override
  public boolean containsError() {
    return this.containsError;
  }
}
