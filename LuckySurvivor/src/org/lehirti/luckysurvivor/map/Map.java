package org.lehirti.luckysurvivor.map;

import static org.lehirti.engine.gui.Key.*;
import static org.lehirti.luckysurvivor.map.Map.Location.*;
import static org.lehirti.luckysurvivor.map.Map.PathDescription.*;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.EventFactory;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class Map extends EventNode<NullState> {
  public static enum Location implements TextKey, ImageKey {
    CRASH_SITE(new CrashSiteLocationFactory()),
    CLIFF_WEST(new CliffWestLocationFactory()),
    JUNGLE_1_UPHILL(new Jungle1UphillLocationFactory()),
    CLIFF_NORTH(new CliffNorthLocationFactory()),
    LOOKOUT_HILL(new LookoutHillLocationFactory()),
    CLIFF_SOUTH(new CliffSouthLocationFactory()),
    JUNGLE_2_UPHILL(new Jungle2UphillLocationFactory()),
    PENINSULA_BASIN_JUNGLE(new PeninsulaBasinJungleLocationFactory());
    
    private final EventFactory locationEventFactory;
    
    private Location(final EventFactory locationEventFactory) {
      this.locationEventFactory = locationEventFactory;
    }
    
    public Event<?> getLocationEvent() {
      return this.locationEventFactory.getInstance();
    }
  }
  
  public static enum PathDescription implements TextKey {
    CLIFF_WEST_2_CRASH_SITE,
    CRASH_SITE_2_CLIFF_WEST,
    JUNGLE_1_UPHILL_2_CRASH_SITE,
    CRASH_SITE_2_JUNGLE_1_UPHILL,
    LOOKOUT_HILL_2_CRASH_SITE,
    CRASH_SITE_2_LOOKOUT_HILL;
  }
  
  // TODO travel time/fatigue
  public static enum Path implements TextKey, ImageKey {
    CLIFF_WEST___CRASH_SITE(CLIFF_WEST, OPTION_EAST, CLIFF_WEST_2_CRASH_SITE, OPTION_WEST, CRASH_SITE_2_CLIFF_WEST, CRASH_SITE),
    JUNGLE_1_UPHILL___CRASH_SITE(JUNGLE_1_UPHILL, OPTION_SOUTH, JUNGLE_1_UPHILL_2_CRASH_SITE, OPTION_NORTH, CRASH_SITE_2_JUNGLE_1_UPHILL, CRASH_SITE),
    CRASH_SITE___LOOKOUT_HILL(CRASH_SITE, OPTION_SOUTH, CRASH_SITE_2_LOOKOUT_HILL, OPTION_NORTH, LOOKOUT_HILL_2_CRASH_SITE, LOOKOUT_HILL);
    
    public final Location loc1;
    public final Location loc2;
    public final Key loc1toloc2;
    public final TextKey loc1toloc2Description;
    public final Key loc2toloc1;
    public final TextKey loc2toloc1Description;
    
    // unidirectional path
    private Path(final Location loc1, final Key loc1toloc2, final TextKey loc1toloc2Description, final Location loc2) {
      this(loc1, loc1toloc2, loc1toloc2Description, null, null, loc2);
    }
    
    // bidirectional path
    private Path(final Location loc1, final Key loc1toloc2, final TextKey loc1toloc2Description, final Key loc2toloc1,
        final TextKey loc2toloc1Description, final Location loc2) {
      this.loc1 = loc1;
      this.loc2 = loc2;
      this.loc1toloc2 = loc1toloc2;
      this.loc1toloc2Description = loc1toloc2Description;
      this.loc2toloc1 = loc2toloc1;
      this.loc2toloc1Description = loc2toloc1Description;
    }
  }
  
  private final Location currentLocation;
  private final Path currentPath;
  private final Location toLocation;
  
  public Map(final Location currentLocation) {
    this.currentLocation = currentLocation;
    this.currentPath = null;
    this.toLocation = null;
  }
  
  public Map(final Path currentPath, final Location toLocation) {
    this.currentLocation = null;
    this.currentPath = currentPath;
    this.toLocation = toLocation;
  }
  
  @Override
  protected void doEvent() {
    if (this.currentLocation != null) {
      setText(this.currentLocation);
      
      for (final Path path : Path.values()) {
        if (path.loc1 == this.currentLocation) {
          addOption(path.loc1toloc2, path.loc1toloc2Description, new Map(path, path.loc2));
        }
        if (path.loc2 == this.currentLocation) {
          addOption(path.loc2toloc1, path.loc2toloc1Description, new Map(path, path.loc1));
        }
      }
      
      addOption(Key.OPTION_ENTER, CommonText.OPTION_EXPLORE_AREA, this.currentLocation.getLocationEvent());
    } else {
      setText(this.currentPath);
      Key optionKey;
      if (this.currentPath.loc1 == this.toLocation) {
        optionKey = this.currentPath.loc2toloc1;
      } else {
        optionKey = this.currentPath.loc1toloc2;
      }
      addOption(optionKey, CommonText.OPTION_NEXT, new Map(this.toLocation));
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.currentLocation != null) {
      return ImgChange.setBGAndFG(this.currentLocation);
    } else {
      return ImgChange.setBGAndFG(this.currentPath);
    }
  }
}
