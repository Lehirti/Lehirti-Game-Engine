package lge.xmlevents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public final class JNewEvent extends JTextField implements EventValidateable {
  private static final long serialVersionUID = 1L;
  
  private final List<String> impossibleEvents;
  
  private String initialValue = null;
  
  private boolean containsError = false;
  
  private static final Pattern EVENT_REF_RESTRICTION_PATTERN = Pattern
      .compile("([a-z][a-z_0-9]*\\.)+[A-Z][a-zA-Z_0-9]*");
  
  public JNewEvent(final List<String> impossibleEvents) {
    setPreferredSize(new Dimension(600, 16));
    this.impossibleEvents = impossibleEvents;
    
    getDocument().addDocumentListener(new DocumentListener() {
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
        adjustBackgroundColor(getText());
      }
    });
  }
  
  public void setInitialValue(final String value) {
    this.initialValue = value;
    setText(value);
  }
  
  private void adjustBackgroundColor(final String text) {
    if (text.equals(this.initialValue)) {
      setBackground(null);
      this.containsError = false;
      setToolTipText("Event name unchanged.");
    } else if (this.impossibleEvents.contains(text)) {
      setBackground(Color.RED);
      this.containsError = true;
      setToolTipText("Event already exists.");
    } else if (!EVENT_REF_RESTRICTION_PATTERN.matcher(text).matches()) {
      setBackground(Color.RED);
      this.containsError = true;
      setToolTipText("Event name does not match required restriction pattern: "
          + EVENT_REF_RESTRICTION_PATTERN.pattern());
    } else {
      setBackground(Color.YELLOW);
      this.containsError = false;
      setToolTipText("Event name changed. Event will be named. Links to this event might \"break\".");
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
