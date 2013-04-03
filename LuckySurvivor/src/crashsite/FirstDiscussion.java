package crashsite;

import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

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
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.FIRST_DISCUSSION_GROUP);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FIRST_DISCUSSION_DESCRIPTION);
    addOption(Key.OPTION_NORTH, Text.OPTION_INJURIES, new StandardEvent(Key.OPTION_NORTH,
        CrashSite.FIRST_DISCUSSION_TOPIC_1, Text.INJURIES, new FirstDiscussion()));
    addOption(Key.OPTION_A, Text.OPTION_OTHER_SURVIVORS, new StandardEvent(Key.OPTION_A,
        CrashSite.FIRST_DISCUSSION_TOPIC_2, Text.OTHER_SURVIVORS, new FirstDiscussion()));
    addOption(Key.OPTION_WEST, Text.OPTION_OTHER_SURVIVORS, new StandardEvent(Key.OPTION_WEST,
        CrashSite.FIRST_DISCUSSION_TOPIC_3, Text.OTHER_SURVIVORS, new FirstDiscussion()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_THE_GIRLS, new StandardEvent(Key.OPTION_SOUTH,
        CrashSite.FIRST_DISCUSSION_TOPIC_4, Text.THE_GIRLS, new FirstDiscussion()));
    addOption(Key.OPTION_EAST, Text.OPTION_ONLY_MALE, new StandardEvent(Key.OPTION_EAST,
        CrashSite.FIRST_DISCUSSION_TOPIC_5, Text.ONLY_MALE, new FirstDiscussion()));
    addOption(Key.OPTION_Z, Text.OPTION_THE_DEAD, new StandardEvent(Key.OPTION_Z, CrashSite.FIRST_DISCUSSION_TOPIC_6,
        Text.THE_DEAD, new FirstDiscussion()));
    addOption(Key.OPTION_X, Text.OPTION_FIND_SHELTER, new StandardEvent(Key.OPTION_X,
        CrashSite.FIRST_DISCUSSION_TOPIC_7, Text.FIND_SHELTER, new FirstDiscussion()));
    addOption(Key.OPTION_C, Text.OPTION_FIND_FOOD_AND_WATER, new StandardEvent(Key.OPTION_C,
        CrashSite.FIRST_DISCUSSION_TOPIC_8, Text.FIND_FOOD_AND_WATER, new FirstDiscussion()));
    addOption(Key.OPTION_Q, Text.OPTION_EXPLORE_ISLAND, new StandardEvent(Key.OPTION_EAST,
        CrashSite.FIRST_DISCUSSION_TOPIC_2, Text.EXPLORE_ISLAND, new FirstDiscussion()));
    addOption(Key.OPTION_V, Text.OPTION_END_DISCUSSION, new MapToCrashSite());
  }
}
