package lge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import lge.res.text.TextParameterNPCResolver;
import lge.state.AbstractState;
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
    Map<String, Object> menuStructure = createMenuStructure(textKeys, new TreeMap<String, Object>());
    JMenu subMenu = new JMenu("Add text");
    add(subMenu);
    addStructureToMenu(subMenu, "", menuStructure);
    
    final List<Class<?>> state = ClassFinder.getSubclassesInFullClasspathStatic(SuperClass.ABSTRACT_STATE);
    menuStructure = createMenuStructure(state, new TreeMap<String, Object>());
    subMenu = new JMenu("Add variable");
    add(subMenu);
    addStructureToMenu(subMenu, "V:", menuStructure);
    
    final List<Class<?>> npcs = ClassFinder.getSubclassesInFullClasspathStatic(SuperClass.TEXT_PARAMETER_NPC_RESOLVER);
    subMenu = new JMenu("Add NPC");
    add(subMenu);
    
    // collect all values that all NPCs have in common for the "current" NPC
    Collection<String> commonNPCValues = null;
    for (final Class<?> npc : npcs) {
      if (Modifier.isFinal(npc.getModifiers())) {
        final Collection<String> npcValues = addNPC(subMenu, npc);
        if (npcValues != null) {
          if (commonNPCValues == null) {
            commonNPCValues = npcValues;
          } else {
            commonNPCValues.retainAll(npcValues);
          }
        }
      }
    }
    
    if (commonNPCValues != null) {
      final String prefix = "#";
      final JMenu npcMenu = new JMenu(prefix);
      for (final String name : commonNPCValues) {
        final JMenuItem item = new JMenuItem(name);
        item.setName(prefix + name);
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
        npcMenu.add(item);
      }
      
      subMenu.add(npcMenu);
    }
  }
  
  private Collection<String> addNPC(final JMenu topMenu, final Class<?> npc) {
    TextParameterNPCResolver npcInstance;
    try {
      npcInstance = (TextParameterNPCResolver) npc.getConstructor().newInstance();
      final String prefix = npcInstance.getParameterPrefix();
      final JMenu npcMenu = new JMenu(prefix);
      
      final Field field = npc.getDeclaredField("STATE_BY_NAME_MAP");
      field.setAccessible(true);
      final Map<String, AbstractState> object = (Map<String, AbstractState>) field.get(null);
      field.setAccessible(false);
      for (final String name : object.keySet()) {
        final JMenuItem item = new JMenuItem(name);
        item.setName(prefix + name);
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
        npcMenu.add(item);
      }
      
      topMenu.add(npcMenu);
      return new HashSet<String>(object.keySet());
    } catch (final InstantiationException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final IllegalAccessException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final IllegalArgumentException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final InvocationTargetException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final NoSuchMethodException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final SecurityException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (final NoSuchFieldException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
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
  
  private static Map<String, Object> createMenuStructure(final List<Class<?>> classes,
      final Map<String, Object> menuStructure) {
    for (final Class<?> clazz : classes) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      Map<String, Object> subStructure = menuStructure;
      
      for (final String nameFragment : clazz.getName().split("\\.")) {
        final Object subSubStructure = subStructure.get(nameFragment);
        if (subSubStructure instanceof Map) {
          subStructure = (Map<String, Object>) subSubStructure;
        } else if (subSubStructure == null) {
          final Map<String, Object> newSubStructure = new TreeMap<>();
          subStructure.put(nameFragment, newSubStructure);
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
