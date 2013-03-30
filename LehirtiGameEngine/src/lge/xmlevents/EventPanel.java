package lge.xmlevents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import lge.jaxb.Event;
import lge.jaxb.Event.Extensions;
import lge.jaxb.Event.Extensions.Extension;
import lge.jaxb.Event.Images;
import lge.jaxb.Event.Options;
import lge.jaxb.Event.Options.Option;

public class EventPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JTextField packageName = new JTextField();
  private final JTextField eventName = new JTextField();
  
  private final JTextArea description = new JTextArea();
  private final JTextArea todo = new JTextArea();
  
  private final JPanel extensions = new JPanel();
  private final JButton addExtentionsButton = new JButton("Add extension");
  
  private final JCheckBox clearBG = new JCheckBox();
  private final JCheckBox clearFG = new JCheckBox();
  private final JTextField bgImage = new JTextField();
  private final JPanel bgImageContainer = new JPanel();
  
  private final JPanel fgImages = new JPanel();
  private final JButton addFGImageButton = new JButton("Add foreground image");
  
  private final JPanel texts = new JPanel();
  private final JButton addtextButton = new JButton("Add text");
  
  private final JPanel options = new JPanel();
  private final JButton addOptionButton = new JButton("Add option");
  
  public EventPanel(final String packageName, final String eventName, final Event event,
      final List<String> allClassEvents, final List<String> allCreatableClassEvents, final Set<String> allXMLEvents) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    addTextFieldWithLabel("Package name", this.packageName, packageName);
    addTextFieldWithLabel("Event name", this.eventName, eventName);
    addTextFieldWithLabel("Description", this.description, event.getDescription());
    addTextFieldWithLabel("TODO", this.todo, event.getTodo());
    
    this.extensions.setLayout(new FlowLayout());
    add(this.extensions);
    final Extensions exts = event.getExtensions();
    if (exts != null) {
      for (final Extension ext : exts.getExtension()) {
        this.extensions.add(new ExtensionPanel(ext, allClassEvents));
      }
    }
    this.addExtentionsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        EventPanel.this.extensions.add(new ExtensionPanel(new Extension(), allClassEvents));
        EventPanel.this.extensions.validate();
        EventPanel.this.validate();
        EventPanel.this.repaint();
      }
    });
    add(this.addExtentionsButton);
    
    Images imgs = event.getImages();
    if (imgs == null) {
      imgs = new Images();
    }
    this.bgImageContainer.setLayout(new FlowLayout());
    add(this.bgImageContainer);
    this.bgImageContainer.add(new JLabel("Clear Background"));
    this.bgImageContainer.add(this.clearBG);
    this.bgImageContainer.add(new JLabel("Clear Foreground"));
    this.bgImageContainer.add(this.clearFG);
    this.bgImageContainer.add(new JLabel("Background image"));
    this.bgImageContainer.add(this.bgImage);
    final String bg = imgs.getBg();
    if (bg != null) {
      this.bgImage.setText(bg);
    }
    this.bgImage.setPreferredSize(new Dimension(500, 16));
    
    this.fgImages.setLayout(new FlowLayout());
    add(this.fgImages);
    for (final String fg : imgs.getFg()) {
      this.fgImages.add(new FGImagePanel(fg));
    }
    this.addFGImageButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        EventPanel.this.fgImages.add(new FGImagePanel(""));
        EventPanel.this.fgImages.validate();
        EventPanel.this.validate();
        EventPanel.this.repaint();
      }
    });
    add(this.addFGImageButton);
    
    this.texts.setLayout(new FlowLayout());
    add(this.texts);
    final Event.Texts evTexts = event.getTexts();
    if (evTexts != null) {
      for (final String text : evTexts.getText()) {
        this.texts.add(new TextPanel(text));
      }
    }
    this.addtextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        EventPanel.this.texts.add(new TextPanel(""));
        EventPanel.this.texts.validate();
        EventPanel.this.validate();
        EventPanel.this.repaint();
      }
    });
    add(this.addtextButton);
    
    this.options.setLayout(new FlowLayout());
    add(this.options);
    final Options opts = event.getOptions();
    if (opts != null) {
      for (final Option opt : opts.getOption()) {
        this.options.add(new OptionPanel(opt, allCreatableClassEvents, allXMLEvents));
      }
    }
    this.addOptionButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        EventPanel.this.options.add(new OptionPanel(new Option(), allCreatableClassEvents, allXMLEvents));
        EventPanel.this.options.validate();
        EventPanel.this.validate();
        EventPanel.this.repaint();
      }
    });
    add(this.addOptionButton);
  }
  
  private void addTextFieldWithLabel(final String labelText, final JTextArea field, final String fieldValue) {
    final JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add(new JLabel(labelText));
    field.setPreferredSize(new Dimension(1000, 160));
    field.setText(fieldValue);
    panel.add(field);
    add(panel);
  }
  
  private void addTextFieldWithLabel(final String labelText, final JTextField field, final String fieldValue) {
    final JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add(new JLabel(labelText));
    field.setPreferredSize(new Dimension(1000, 16));
    field.setText(fieldValue);
    panel.add(field);
    add(panel);
  }
  
  public String getPackageName() {
    return this.packageName.getText();
  }
  
  public String getEventName() {
    return this.eventName.getText();
  }
  
  public Event getEvent() {
    final Event event = new Event();
    event.setDescription(this.description.getText());
    event.setTodo(this.todo.getText());
    
    final List<Extension> exts = new LinkedList<>();
    for (final Component component : this.extensions.getComponents()) {
      final ExtensionPanel ep = (ExtensionPanel) component;
      final Extension extension = ep.getExtension();
      exts.add(extension);
    }
    if (!exts.isEmpty()) {
      final Event.Extensions evExtensions = new Event.Extensions();
      evExtensions.getExtension().addAll(exts);
      event.setExtensions(evExtensions);
    }
    
    final Images imgs = new Images();
    imgs.setClearBackground(Boolean.valueOf(this.clearBG.isSelected()));
    imgs.setClearForeground(Boolean.valueOf(this.clearFG.isSelected()));
    final String bgImg = this.bgImage.getText();
    if (bgImg.trim().length() > 0) {
      imgs.setBg(bgImg);
    }
    for (final Component component : this.fgImages.getComponents()) {
      final FGImagePanel fgp = (FGImagePanel) component;
      final String fgImage = fgp.getFGImage();
      imgs.getFg().add(fgImage);
    }
    event.setImages(imgs);
    
    final List<String> textList = new LinkedList<>();
    for (final Component component : this.texts.getComponents()) {
      final TextPanel tp = (TextPanel) component;
      final String text = tp.getText();
      textList.add(text);
    }
    final Event.Texts evTexts = new Event.Texts();
    evTexts.getText().addAll(textList);
    event.setTexts(evTexts);
    
    final List<Option> opts = new LinkedList<>();
    for (final Component component : this.options.getComponents()) {
      final OptionPanel op = (OptionPanel) component;
      final Option opt = op.getOption();
      opts.add(opt);
    }
    final Event.Options evOptions = new Event.Options();
    evOptions.getOption().addAll(opts);
    event.setOptions(evOptions);
    
    return event;
  }
  
  public boolean containsValidEvent() {
    final Event event = getEvent();
    try {
      XMLEventsHelper.validate(event);
      return true;
    } catch (final JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }
  
  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getPreferredSize() {
    return getParent().getSize();
  }
}
