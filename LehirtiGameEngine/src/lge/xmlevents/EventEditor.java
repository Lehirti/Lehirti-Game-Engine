package lge.xmlevents;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lge.gui.WindowCloseListener;
import lge.gui.WindowLocation;
import lge.gui.WindowLocation.WinLoc;
import lge.jaxb.Event;
import lge.jaxb.Event.Extensions.Extension;
import lge.jaxb.Event.Options.Option;
import lge.util.EventClassHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEditor extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(EventEditor.class);
  
  private static final String SIMPLE_NAME_OF_NEW_EVENT = "NewEvent";
  
  private String currentEventPackage;
  private String currentEventName;
  private Event currentEvent;
  
  /**
   * all events which are present as XML events
   */
  private final Map<String, Event> allXMLEvents;
  
  /**
   * all events which are present as .class files (excluding generated-from-xml .class files)
   */
  private final List<String> allClassEvents;
  
  /**
   * <p>
   * all event which are present as .class files (excluding generated-from-xml .class files) and only have a default
   * constructor, meaning you can get to them from xml events.
   * </p>
   * note: event classes always have a default constructor for load/save purposes, so having a default constructor is
   * not enough. the default constructor must be the only constructor.
   */
  private final List<String> allCreatableClassEvents;
  
  JPanel all = new JPanel();
  
  DefaultListModel<String> xmlElementsModel = new DefaultListModel<>();
  JList<String> xmlElements = new JList<>(this.xmlElementsModel);
  ListSelectionListener xmlElementsListener;
  
  EventPanel eventComponent = null;
  
  public EventEditor(final String fqpnOfCurrentClass, final String simpleNameOfCurrentEvent) {
    this.allXMLEvents = XMLEventsHelper.readExistingXMLEvents();
    
    final String fqcnOfCurrentEvent = fqpnOfCurrentClass + "." + simpleNameOfCurrentEvent;
    final String fqcnOfNewEvent = fqpnOfCurrentClass + "." + SIMPLE_NAME_OF_NEW_EVENT;
    
    this.currentEvent = this.allXMLEvents.get(fqcnOfNewEvent);
    if (this.currentEvent == null) {
      setCurrentEvent(initializeNewEvent(fqcnOfCurrentEvent), fqpnOfCurrentClass, SIMPLE_NAME_OF_NEW_EVENT);
    } else {
      setCurrentEvent(this.currentEvent, fqcnOfNewEvent);
    }
    
    this.allClassEvents = EventClassHelper.getEventClassFQCNs(false);
    
    this.allCreatableClassEvents = EventClassHelper.getEventClassFQCNs(true);
    
    this.xmlElementsListener = new ListSelectionListener() {
      @Override
      public void valueChanged(final ListSelectionEvent e) {
        final String fqcn = EventEditor.this.xmlElements.getSelectedValue();
        if (fqcn != null) {
          if (EventEditor.this.eventComponent.containsValidEvent()) {
            final Event event = EventEditor.this.allXMLEvents.get(fqcn);
            EventEditor.this.setCurrentEvent(event, fqcn);
            setCurrentEventToScreen();
          } else {
            EventEditor.this.xmlElements.setSelectedValue(EventEditor.this.currentEventPackage + "."
                + EventEditor.this.currentEventName, true);
          }
        }
      }
    };
    
    this.xmlElements.addListSelectionListener(this.xmlElementsListener);
    
    this.all.setLayout(new BorderLayout());
    this.all.add(this.xmlElements, BorderLayout.WEST);
    setCurrentEventToScreen();
    
    getContentPane().add(this.all, BorderLayout.CENTER);
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setTitle("Event Editor");
    
    pack();
    
    setVisible(true);
    
    final WinLoc winLoc = WindowLocation.getLocationFor(EventEditor.class.getSimpleName());
    if (!winLoc.isMax) {
      setLocation(winLoc.x, winLoc.y);
      setSize(winLoc.width, winLoc.height);
    } else {
      setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    addWindowListener(new WindowCloseListener(this, EventEditor.class.getSimpleName()) {
      @Override
      public void windowClosing(final WindowEvent e) {
        EventEditor.this.updateEventFromScreen();
        super.windowClosing(e);
        XMLEventsHelper.writeXMLEvents(EventEditor.this.allXMLEvents, true);
        XMLEventsHelper.buildAll();
      }
    });
  }
  
  private void initXMLElementsModel(final String fqcnOfNewEvent) {
    this.xmlElements.removeListSelectionListener(this.xmlElementsListener);
    this.xmlElementsModel.clear();
    int index = 0;
    for (final String element : this.allXMLEvents.keySet()) {
      this.xmlElementsModel.addElement(element);
      if (element.equals(fqcnOfNewEvent)) {
        this.xmlElements.setSelectedIndex(index);
      }
      index++;
    }
    this.xmlElements.addListSelectionListener(this.xmlElementsListener);
  }
  
  private void setCurrentEvent(final Event event, final String fqcnOfEvent) {
    final int i = fqcnOfEvent.lastIndexOf(".");
    setCurrentEvent(event, fqcnOfEvent.substring(0, i), fqcnOfEvent.substring(i + 1));
  }
  
  private void setCurrentEvent(final Event event, final String fqpnEvent, final String simpleNameOfEvent) {
    // save old event
    final boolean updateXMLElementsModel = updateEventFromScreen();
    
    // set current event
    this.currentEventPackage = fqpnEvent;
    this.currentEventName = simpleNameOfEvent;
    this.currentEvent = event;
    this.allXMLEvents.put(fqpnEvent + "." + simpleNameOfEvent, this.currentEvent);
    if (updateXMLElementsModel) {
      initXMLElementsModel(fqpnEvent + "." + simpleNameOfEvent);
    }
  }
  
  private boolean updateEventFromScreen() {
    boolean updateXMLElementsModel = true;
    if (this.eventComponent != null) {
      final String packageName = this.eventComponent.getPackageName();
      final String eventName = this.eventComponent.getEventName();
      if (this.currentEventPackage != null && this.currentEventName != null) {
        this.allXMLEvents.remove(this.currentEventPackage + "." + this.currentEventName);
        if (this.currentEventPackage.equals(packageName) && this.currentEventName.equals(eventName)) {
          updateXMLElementsModel = false;
        }
      }
      
      final Event oldEvent = this.eventComponent.getEvent();
      if (oldEvent != null) {
        this.allXMLEvents.put(packageName + "." + eventName, oldEvent);
      }
    }
    return updateXMLElementsModel;
  }
  
  private void setCurrentEventToScreen() {
    if (this.eventComponent != null) {
      this.all.remove(this.eventComponent);
    }
    this.eventComponent = new EventPanel(this.currentEventPackage, this.currentEventName, this.currentEvent,
        this.allClassEvents, this.allCreatableClassEvents, this.allXMLEvents.keySet());
    this.all.add(this.eventComponent, BorderLayout.CENTER);
    validate();
    repaint();
  }
  
  private static Event initializeNewEvent(final String fqcnOfCurrentEvent) {
    final Event event = new Event();
    
    // extend current event to lead to new event
    final Event.Extensions exts = new Event.Extensions();
    final Extension ext = new Extension();
    ext.setEvent(fqcnOfCurrentEvent);
    ext.setText("OPTION_TO_NEW_EVENT");
    exts.getExtension().add(ext);
    event.setExtensions(exts);
    final Event.Texts texts = new Event.Texts();
    texts.getText().add("NEW_EVENT_TEXT");
    event.setTexts(texts);
    final Event.Options opts = new Event.Options();
    final Option opt = new Option();
    opt.setText("OPTION_BACK");
    opt.setEvent(fqcnOfCurrentEvent);
    opts.getOption().add(opt);
    event.setOptions(opts);
    
    return event;
  }
  
  public void actionPerformed(final ActionEvent e) {
  }
}
