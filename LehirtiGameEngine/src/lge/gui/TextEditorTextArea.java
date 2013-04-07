package lge.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

public class TextEditorTextArea extends JTextArea {
  private static final long serialVersionUID = 1L;
  
  private final class PopupAction implements Action {
    @Override
    public void actionPerformed(final ActionEvent e) {
      TextAreaParameterPopup.getInstance().show(TextEditorTextArea.this, 0, 0);
    }
    
    @Override
    public Object getValue(final String key) {
      return null;
    }
    
    @Override
    public void putValue(final String key, final Object value) {
    }
    
    @Override
    public void setEnabled(final boolean b) {
    }
    
    @Override
    public boolean isEnabled() {
      return true;
    }
    
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
    }
    
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
    }
  }
  
  public TextEditorTextArea() {
    setLineWrap(true);
    setWrapStyleWord(true);
    setBorder(new LineBorder(Color.BLACK));
    
    getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_DOWN_MASK),
        "actionMapKey");
    getActionMap().put("actionMapKey", new PopupAction());
  }
}
