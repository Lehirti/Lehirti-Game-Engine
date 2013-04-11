package crashsite;

import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.events.SetFlagEvent;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.state.BoolState;
import lge.state.State;

public class FirstDiscussion extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FIRST_DISCUSSION_DESCRIPTION,
    OPTION_INJURIES,
    INJURIES,
    OPTION_OTHER_SURVIVORS,
    OTHER_SURVIVORS,
    OPTION_THE_GIRLS,
    THE_GIRLS,
    OPTION_ONLY_MALE,
    ONLY_MALE,
    OPTION_THE_DEAD,
    THE_DEAD,
    OPTION_FIND_SHELTER,
    FIND_SHELTER,
    OPTION_FIND_FOOD_AND_WATER,
    FIND_FOOD_AND_WATER,
    OPTION_EXPLORE_ISLAND,
    EXPLORE_ISLAND,
    OPTION_END_DISCUSSION,
    END_DISCUSSION;
  }
  
  // all those flags are initially "false"
  public static enum Bool implements BoolState {
    HAS_DISCUSSED_INJURIES,
    HAS_DISCUSSED_OTHER_SURVIVORS,
    HAS_DISCUSSED_OTHER_SURVIVORS_AGAIN,
    HAS_DISCUSSED_THE_GIRLS,
    HAS_DISCUSSED_ONLY_MALE,
    HAS_DISCUSSED_THE_DEAD,
    HAS_DISCUSSED_FIND_SHELTER,
    HAS_DISCUSSED_FIND_FOOD_AND_WATER,
    HAS_DISCUSSED_EXPLORE_ISLAND,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.FIRST_DISCUSSION_GROUP);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FIRST_DISCUSSION_DESCRIPTION);
    // while discussing the respective option, the flag is set to "true"
    addOption(Key.OPTION_NORTH, Text.OPTION_INJURIES, new SetFlagEvent(Bool.HAS_DISCUSSED_INJURIES, Key.OPTION_NORTH,
        CrashSite.FIRST_DISCUSSION_TOPIC_1, Text.INJURIES, new FirstDiscussion()));
    addOption(Key.OPTION_A, Text.OPTION_OTHER_SURVIVORS, new SetFlagEvent(Bool.HAS_DISCUSSED_OTHER_SURVIVORS,
        Key.OPTION_A, CrashSite.FIRST_DISCUSSION_TOPIC_2, Text.OTHER_SURVIVORS, new FirstDiscussion()));
    addOption(Key.OPTION_WEST, Text.OPTION_OTHER_SURVIVORS, new SetFlagEvent(Bool.HAS_DISCUSSED_OTHER_SURVIVORS_AGAIN,
        Key.OPTION_WEST, CrashSite.FIRST_DISCUSSION_TOPIC_3, Text.OTHER_SURVIVORS, new FirstDiscussion()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_THE_GIRLS, new SetFlagEvent(Bool.HAS_DISCUSSED_THE_GIRLS, Key.OPTION_SOUTH,
        CrashSite.FIRST_DISCUSSION_TOPIC_4, Text.THE_GIRLS, new FirstDiscussion()));
    addOption(Key.OPTION_EAST, Text.OPTION_ONLY_MALE, new SetFlagEvent(Bool.HAS_DISCUSSED_ONLY_MALE, Key.OPTION_EAST,
        CrashSite.FIRST_DISCUSSION_TOPIC_5, Text.ONLY_MALE, new FirstDiscussion()));
    addOption(Key.OPTION_Z, Text.OPTION_THE_DEAD, new SetFlagEvent(Bool.HAS_DISCUSSED_THE_DEAD, Key.OPTION_Z,
        CrashSite.FIRST_DISCUSSION_TOPIC_6, Text.THE_DEAD, new FirstDiscussion()));
    addOption(Key.OPTION_X, Text.OPTION_FIND_SHELTER, new SetFlagEvent(Bool.HAS_DISCUSSED_FIND_SHELTER, Key.OPTION_X,
        CrashSite.FIRST_DISCUSSION_TOPIC_7, Text.FIND_SHELTER, new FirstDiscussion()));
    addOption(Key.OPTION_C, Text.OPTION_FIND_FOOD_AND_WATER, new SetFlagEvent(Bool.HAS_DISCUSSED_FIND_FOOD_AND_WATER,
        Key.OPTION_C, CrashSite.FIRST_DISCUSSION_TOPIC_8, Text.FIND_FOOD_AND_WATER, new FirstDiscussion()));
    addOption(Key.OPTION_Q, Text.OPTION_EXPLORE_ISLAND, new SetFlagEvent(Bool.HAS_DISCUSSED_EXPLORE_ISLAND,
        Key.OPTION_EAST, CrashSite.FIRST_DISCUSSION_TOPIC_2, Text.EXPLORE_ISLAND, new FirstDiscussion()));
    
    // only after all flags are set to "true" is the "end discussion" option shown
    if (State.isAll(Bool.values())) {
      addOption(Key.OPTION_V, Text.OPTION_END_DISCUSSION, new MapToCrashSite());
    }
  }
}
