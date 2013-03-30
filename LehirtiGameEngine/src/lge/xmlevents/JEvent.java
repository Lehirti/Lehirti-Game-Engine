package lge.xmlevents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public final class JEvent extends JComboBox<String> {
  private static final long serialVersionUID = 1L;
  
  public JEvent(final String[] possibleEvents, final boolean isEditable) {
    super(possibleEvents);
    setEditable(isEditable);
    setPreferredSize(new Dimension(400, 16));
    
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
          adjustBackgroundColor();
        }
      });
    }
  }
  
  private void adjustBackgroundColor(final String text) {
    boolean found = false;
    for (int i = 0; i < getModel().getSize(); i++) {
      if (getModel().getElementAt(i).equals(text)) {
        found = true;
        break;
      }
    }
    if (!found) {
      setBackground(Color.YELLOW);
      getEditor().getEditorComponent().setBackground(Color.YELLOW);
    } else {
      setBackground(null);
      getEditor().getEditorComponent().setBackground(Color.WHITE);
    }
  }
  
  private void adjustBackgroundColor() {
    if (getSelectedIndex() == -1) {
      setBackground(Color.YELLOW);
      getEditor().getEditorComponent().setBackground(Color.YELLOW);
    } else {
      setBackground(null);
      getEditor().getEditorComponent().setBackground(Color.WHITE);
    }
  }
}
