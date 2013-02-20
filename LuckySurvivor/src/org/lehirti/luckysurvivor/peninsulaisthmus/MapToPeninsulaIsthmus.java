package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.islandentry.MapToIslandEntry;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

/**
 * this is the default first event when entering
 */
public class MapToPeninsulaIsthmus extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    LEAVE_THE_AREA,
    OPTION_SEARCH_FOR_A_WAY,
    SEARCH_FOR_A_WAY,
    FIND_TREE,
    OPTION_FELL_TREE,
    FELL_TREE,
    OPTION_GO_EAST,
    TEXT_NO_WAY_ACROSS,
  }
  
  @Override
  /*
   * changes to the image area are controlled via this method. after the previous event has finished executing, this
   * method is called for ALL possible next events (ALL the options) to preload all images that might be needed next.
   * this means that this method is even called for events that will not be executed (because the player selects another
   * option). it is therefore strictly FORBIDDEN to update state (set a variable) in this method. however, you are - of
   * course - free the query state as much as you want, just keep in mind that the event counter for this Event and all
   * the updates in the doEvent() method happen AFTER this method is called.
   */
  protected ImgChange updateImageArea() {
    // your use case with the tree is simple:
    if (State.getEventCount(FellTree.class) == 0) {
      // as long as the tree is not felled, show the without-tree image
      return ImgChange.setBGAndFG(PeninsulaIsthmus.PENINSULA_ISTHMUS);
    } else {
      // later, show the with-tree image
      return ImgChange.setBGAndFG(PeninsulaIsthmus.PENINSULA_ISTHMUS_WITH_TREE);
    }
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    // as long as no way is found
    if (State.getEventCount(FindTree.class) == 0) {
      // search for a ways
      addOption(Key.OPTION_WEST, Text.OPTION_SEARCH_FOR_A_WAY, new SearchForAWay());
      
      // once a suitable tree is found, make felling it available as a one-time event
    } else if (State.getEventCount(FellTree.class) == 0) {
      addOption(Key.OPTION_WEST, Text.OPTION_FELL_TREE, new FellTree());
    }
    
    // as long as there is no path across
    if (State.getEventCount(FellTree.class) == 0) {
      // THIS ... IS ... CUCUMBERLAND
      addOption(Key.OPTION_EAST, Text.OPTION_GO_EAST, new GoEastWithoutTree());
    } else {
      // once the tree is felled, go east
      addOption(Key.OPTION_EAST, Text.OPTION_GO_EAST, new MapToIslandEntry());
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.PENINSULA_ISTHMUS)));
  }
}
