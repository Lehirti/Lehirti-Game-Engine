package lge.xmlevents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public final class JEvent extends JComboBox<String> implements EventValidateable {
  private static final long serialVersionUID = 1L;
  
  private final List<String> impossibleEvents;
  
  private static final Pattern EVENT_REF_RESTRICTION_PATTERN = Pattern
      .compile("([a-z][a-z_0-9]*\\.)+[A-Z][a-zA-Z_0-9]*");
  
  private boolean containsError = false;
  
  private boolean canBeCreated = false;
  
  public JEvent(final String[] possibleEvents, final List<String> impossibleEvents, final boolean isEditable) {
    super(possibleEvents);
    setEditable(isEditable);
    setPreferredSize(new Dimension(400, 16));
    this.impossibleEvents = impossibleEvents;
    
    if (isEditable) {
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
    }
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
      if (this.impossibleEvents.contains(value)) {
        getEditor().getEditorComponent().setBackground(Color.RED);
        this.containsError = true;
        this.canBeCreated = false;
        setToolTipText("Event exists, but it cannot be created without parameters, so it's not accessible from XML events.");
      } else if (!EVENT_REF_RESTRICTION_PATTERN.matcher(value).matches()) {
        getEditor().getEditorComponent().setBackground(Color.RED);
        this.containsError = true;
        this.canBeCreated = false;
        setToolTipText("Field value \"" + value + "\" does not match required pattern \""
            + EVENT_REF_RESTRICTION_PATTERN.pattern() + "\".");
      } else {
        getEditor().getEditorComponent().setBackground(Color.YELLOW);
        this.containsError = false;
        this.canBeCreated = true;
        setToolTipText("Event does not exists, but it can be created as another XML event.");
      }
    } else {
      getEditor().getEditorComponent().setBackground(Color.WHITE);
      this.containsError = false;
      this.canBeCreated = false;
      setToolTipText("Event exists, and can be used.");
    }
    Container ancestor = getParent();
    while (ancestor != null && !(ancestor instanceof EventPanel)) {
      ancestor = ancestor.getParent();
    }
    if (ancestor != null) {
      ((EventPanel) ancestor).updateStatus();
    }
  }
  
  public boolean canBeCreated() {
    return this.canBeCreated;
  }
  
  @Override
  public boolean containsError() {
    return this.containsError;
  }
}
