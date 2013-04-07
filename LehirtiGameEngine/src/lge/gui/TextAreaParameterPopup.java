package lge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import lge.util.ClassFinder;
import lge.util.ClassFinder.SuperClass;

public final class TextAreaParameterPopup extends JPopupMenu {
  private static final long serialVersionUID = 1L;
  
  private static TextAreaParameterPopup INSTANCE;
  
  public static final synchronized TextAreaParameterPopup getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TextAreaParameterPopup();
    }
    return INSTANCE;
  }
  
  private TextAreaParameterPopup() {
    final List<Class<?>> textKeys = ClassFinder.getSubclassesInFullClasspathStatic(SuperClass.TEXT_KEY);
    final Map<String, ?> menuStructure = createMenuStructure(textKeys);
    addStructureToMenu(this, "", menuStructure);
    
    // final String text = "{test}";
    // TextEditorTextArea.this.insert(text, TextEditorTextArea.this.getCaretPosition());
  }
  
  private void addStructureToMenu(final JComponent parent, final String prefix, final Map<String, ?> menuStructure) {
    for (final Map.Entry<String, ?> entry : menuStructure.entrySet()) {
      final Map<String, ?> value = (Map<String, ?>) entry.getValue();
      if (value.isEmpty()) {
        final JMenuItem item = new JMenuItem(entry.getKey());
        item.setName(prefix + entry.getKey());
        item.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            final StringBuilder sb = new StringBuilder("{");
            sb.append(item.getName());
            sb.append("}");
            final JTextArea invoker = (JTextArea) getInvoker();
            invoker.insert(sb.toString(), invoker.getCaretPosition());
          }
        });
        parent.add(item);
      } else {
        final JMenu subMenu = new JMenu(entry.getKey());
        addStructureToMenu(subMenu, prefix + entry.getKey() + ".", value);
        parent.add(subMenu);
      }
    }
  }
  
  private static Map<String, ?> createMenuStructure(final List<Class<?>> textKeys) {
    final Map<String, Object> menuStructure = new TreeMap<>();
    for (final Class<?> clazz : textKeys) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      Map<String, Object> subStructure = menuStructure;
      
      for (final String nameFragment : clazz.getName().split("\\.")) {
        final String replacedName = nameFragment.replaceAll("\\$", ".");
        final Object subSubStructure = subStructure.get(replacedName);
        if (subSubStructure instanceof Map) {
          subStructure = (Map<String, Object>) subSubStructure;
        } else if (subSubStructure == null) {
          final Map<String, Object> newSubStructure = new TreeMap<>();
          subStructure.put(replacedName, newSubStructure);
          subStructure = newSubStructure;
        } else {
          // TODO error or end
        }
      }
      
      for (final Object constant : clazz.getEnumConstants()) {
        subStructure.put(((Enum) constant).name(), new HashMap<>());
      }
    }
    return menuStructure;
  }
}
