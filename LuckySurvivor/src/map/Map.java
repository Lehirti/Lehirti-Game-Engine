package map;

import static lge.gui.Key.*;
import static map.Map.Location.*;
import static map.Map.PathDescription.*;
import holeintheground.MapToHoleInTheGround;
import islandentry.MapToIslandEntry;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jungle1uphill.MapToJungle1Uphill;
import jungle2uphill.MapToJungle2Uphill;
import lge.events.Event;
import lge.events.Event.NullState;
import lge.events.EventFactory;
import lge.events.EventNode;
import lge.events.StaticEventFactory;
import lge.gui.Key;
import lge.res.ResourceState;
import lge.res.TextAndImageKeyWithFlag;
import lge.res.images.ImageKey;
import lge.res.images.ImageProxy.ProxyProps;
import lge.res.images.ImageProxyPath;
import lge.res.images.ImageWrapper;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;
import lge.state.TimeInterval;
import lookouthill.MapToLookoutHill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import peninsulabasinjungle.MapToPeninsulaBasinJungle;
import peninsulaisthmus.MapToPeninsulaIsthmus;
import pondoverhang.MapToPondOverhang;
import ridge.MapToRidge;
import cliffmeadowedgewest.MapToCliffMeadowEdgeWest;
import cliffnorth.MapToCliffNorth;
import cliffsouth.MapToCliffSouth;
import cliffwest.MapToCliffWest;
import crashsite.MapToCrashSite;
import creekundercliff.MapToCreekUnderCliff;
import crevicewithcreek.MapToCreviceWithCreek;
import drycreek.MapToDryCreek;

public class Map extends EventNode<NullState> {
  public static enum Location implements TextKey, ImageKey {
    CRASH_SITE(0.545, 3.47, new StaticEventFactory(MapToCrashSite.class)),
    CLIFF_WEST(0.4225, 3.38, new StaticEventFactory(MapToCliffWest.class)),
    JUNGLE_1_UPHILL(0.7275, 3.25, new StaticEventFactory(MapToJungle1Uphill.class)),
    CLIFF_NORTH(0.7225, 3.123333, new StaticEventFactory(MapToCliffNorth.class)),
    LOOKOUT_HILL(0.6575, 3.59333, new StaticEventFactory(MapToLookoutHill.class)),
    CLIFF_SOUTH(0.71, 3.7333333333, new StaticEventFactory(MapToCliffSouth.class)),
    JUNGLE_2_UPHILL(0.81, 3.446666667, new StaticEventFactory(MapToJungle2Uphill.class)),
    PENINSULA_BASIN_JUNGLE(0.985, 3.4833333, new StaticEventFactory(MapToPeninsulaBasinJungle.class)),
    PENINSULA_ISTHMUS(1.18, 3.47, new StaticEventFactory(MapToPeninsulaIsthmus.class)),
    ISLAND_ENTRY(1.26, 3.4466666667, new StaticEventFactory(MapToIslandEntry.class)),
    RIDGE(1.425, 3.73333333333, new StaticEventFactory(MapToRidge.class)),
    CREVICE_WITH_CREEK(1.4425, 3.216666666667, new StaticEventFactory(MapToCreviceWithCreek.class)),
    CREEK_UNDER_CLIFF(1.545, 3.283333333, new StaticEventFactory(MapToCreekUnderCliff.class)),
    POND_OVERHANG(1.6925, 3.1466666667, new StaticEventFactory(MapToPondOverhang.class)),
    DRY_CREEK(1.8525, 3.253333333, new StaticEventFactory(MapToDryCreek.class)),
    CLIFF_MEADOW_EDGE_WEST(2.025, 3.19, new StaticEventFactory(MapToCliffMeadowEdgeWest.class)),
    HOLE_IN_THE_GROUND(2.045, 3.153333333, new StaticEventFactory(MapToHoleInTheGround.class));
    
    private final EventFactory locationEventFactory;
    
    public final double x;
    public final double y;
    
    private Location(final double x, final double y, final EventFactory locationEventFactory) {
      this.x = x;
      this.y = y;
      this.locationEventFactory = locationEventFactory;
    }
    
    public Event<?> getLocationEvent() {
      return this.locationEventFactory.getInstance();
    }
    
    private static final Logger LOCATION_LOGGER = LoggerFactory.getLogger(Location.class);
    
    public static void write(final @Nullable Location location, final @Nonnull ObjectOutput out) throws IOException {
      if (location == null) {
        out.writeObject(null); // input is null
      } else {
        out.writeObject(location.name());
      }
    }
    
    @CheckForNull
    public static Location read(final @Nonnull ObjectInput in) throws ClassNotFoundException, IOException {
      final String name = (String) in.readObject();
      if (name == null) {
        // input was null
        return null;
      }
      
      try {
        return valueOf(name);
      } catch (final RuntimeException e) {
        LOCATION_LOGGER.error("Failed to reconstruct Location " + name, e);
      }
      return null;
    }
  }
  
  public static enum PathDescription implements TextKey {
    CLIFF_WEST_2_CRASH_SITE,
    CRASH_SITE_2_CLIFF_WEST,
    
    JUNGLE_1_UPHILL_2_CRASH_SITE,
    CRASH_SITE_2_JUNGLE_1_UPHILL,
    
    LOOKOUT_HILL_2_CRASH_SITE,
    CRASH_SITE_2_LOOKOUT_HILL,
    
    CLIFF_NORTH_2_JUNGLE_1_UPHILL,
    JUNGLE_1_UPHILL_2_CLIFF_NORTH,
    
    LOOKOUT_HILL_2_CLIFF_SOUTH,
    CLIFF_SOUTH_2_LOOKOUT_HILL,
    
    CRASH_SITE_2_JUNGLE_2_UPHILL,
    JUNGLE_2_UPHILL_2_CRASH_SITE,
    
    JUNGLE_2_UPHILL_2_PENINSULA_BASIN_JUNGLE,
    PENINSULA_BASIN_JUNGLE_2_JUNGLE_2_UPHILL,
    
    JUNGLE_1_UPHILL_2_PENINSULA_BASIN_JUNGLE,
    PENINSULA_BASIN_JUNGLE_2_JUNGLE_1_UPHILL,
    
    LOOKOUT_HILL_2_PENINSULA_BASIN_JUNGLE,
    PENINSULA_BASIN_JUNGLE_2_LOOKOUT_HILL,
    
    PENINSULA_BASIN_JUNGLE_2_PENINSULA_ISTHMUS,
    PENINSULA_ISTHMUS_2_PENINSULA_BASIN_JUNGLE,
    
    PENINSULA_ISTHMUS_2_ISLAND_ENTRY,
    ISLAND_ENTRY_2_PENINSULA_ISTHMUS,
    
    ISLAND_ENTRY_2_RIDGE,
    RIDGE_2_ISLAND_ENTRY,
    
    RIDGE_2_CREVICE_WITH_CREEK,
    CREVICE_WITH_CREEK_2_RIDGE,
    
    CREVICE_WITH_CREEK_2_CREEK_UNDER_CLIFF,
    CREEK_UNDER_CLIFF_2_CREVICE_WITH_CREEK,
    
    CREEK_UNDER_CLIFF_2_POND_OVERHANG,
    POND_OVERHANG_2_CREEK_UNDER_CLIFF,
    
    POND_OVERHANG_2_DRY_CREEK,
    DRY_CREEK_2_POND_OVERHANG,
    
    DRY_CREEK_2_CLIFF_MEADOW_EDGE_WEST,
    CLIFF_MEADOW_EDGE_WEST_2_DRY_CREEK,
    
    CLIFF_MEADOW_EDGE_WEST_2_HOLE_IN_THE_GROUND,
    HOLE_IN_THE_GROUND_2_CLIFF_MEADOW_EDGE_WEST,
  }
  
  // TODO travel time/fatigue; traveling each path takes exactly one hour; can be changed for each path individually
  /**
   * the flag/BoolState of each path indicates whether to path is available. so the Map$Path.properties file indicates
   * the initial availability of paths
   */
  public static enum Path implements TextAndImageKeyWithFlag {
    CLIFF_WEST___CRASH_SITE(CLIFF_WEST, OPTION_EAST, CLIFF_WEST_2_CRASH_SITE, OPTION_WEST, CRASH_SITE_2_CLIFF_WEST, CRASH_SITE, TimeInterval
        .advanceBy(10000)),
    JUNGLE_1_UPHILL___CRASH_SITE(JUNGLE_1_UPHILL, OPTION_SOUTH, JUNGLE_1_UPHILL_2_CRASH_SITE, OPTION_NORTH, CRASH_SITE_2_JUNGLE_1_UPHILL, CRASH_SITE, TimeInterval
        .advanceBy(10000)),
    CRASH_SITE___LOOKOUT_HILL(CRASH_SITE, OPTION_SOUTH, CRASH_SITE_2_LOOKOUT_HILL, OPTION_NORTH, LOOKOUT_HILL_2_CRASH_SITE, LOOKOUT_HILL, TimeInterval
        .advanceBy(10000)),
    CLIFF_NORTH___JUNGLE_1_UPHILL(CLIFF_NORTH, OPTION_SOUTH, CLIFF_NORTH_2_JUNGLE_1_UPHILL, OPTION_NORTH, JUNGLE_1_UPHILL_2_CLIFF_NORTH, JUNGLE_1_UPHILL, TimeInterval
        .advanceBy(10000)),
    LOOKOUT_HILL___CLIFF_SOUTH(LOOKOUT_HILL, OPTION_SOUTH, LOOKOUT_HILL_2_CLIFF_SOUTH, OPTION_NORTH, CLIFF_SOUTH_2_LOOKOUT_HILL, CLIFF_SOUTH, TimeInterval
        .advanceBy(10000)),
    CRASH_SITE___JUNGLE_2_UPHILL(CRASH_SITE, OPTION_EAST, CRASH_SITE_2_JUNGLE_2_UPHILL, OPTION_WEST, JUNGLE_2_UPHILL_2_CRASH_SITE, JUNGLE_2_UPHILL, TimeInterval
        .advanceBy(10000)),
    JUNGLE_2_UPHILL___PENINSULA_BASIN_JUNGLE(JUNGLE_2_UPHILL, OPTION_EAST, JUNGLE_2_UPHILL_2_PENINSULA_BASIN_JUNGLE, OPTION_WEST, PENINSULA_BASIN_JUNGLE_2_JUNGLE_2_UPHILL, PENINSULA_BASIN_JUNGLE, TimeInterval
        .advanceBy(10000)),
    JUNGLE_1_UPHILL___PENINSULA_BASIN_JUNGLE(JUNGLE_1_UPHILL, OPTION_EAST, JUNGLE_1_UPHILL_2_PENINSULA_BASIN_JUNGLE, OPTION_NORTH, PENINSULA_BASIN_JUNGLE_2_JUNGLE_1_UPHILL, PENINSULA_BASIN_JUNGLE, TimeInterval
        .advanceBy(10000)),
    LOOKOUT_HILL___PENINSULA_BASIN_JUNGLE(LOOKOUT_HILL, OPTION_EAST, LOOKOUT_HILL_2_PENINSULA_BASIN_JUNGLE, OPTION_SOUTH, PENINSULA_BASIN_JUNGLE_2_LOOKOUT_HILL, PENINSULA_BASIN_JUNGLE, TimeInterval
        .advanceBy(10000)),
    PENINSULA_BASIN_JUNGLE___PENINSULA_ISTHMUS(PENINSULA_BASIN_JUNGLE, OPTION_EAST, PENINSULA_BASIN_JUNGLE_2_PENINSULA_ISTHMUS, OPTION_WEST, PENINSULA_ISTHMUS_2_PENINSULA_BASIN_JUNGLE, PENINSULA_ISTHMUS, TimeInterval
        .advanceBy(10000)),
    PENINSULA_ISTHMUS___ISLAND_ENTRY(PENINSULA_ISTHMUS, OPTION_EAST, PENINSULA_ISTHMUS_2_ISLAND_ENTRY, OPTION_WEST, ISLAND_ENTRY_2_PENINSULA_ISTHMUS, ISLAND_ENTRY, TimeInterval
        .advanceBy(10000)),
    ISLAND_ENTRY___RIDGE(ISLAND_ENTRY, OPTION_EAST, ISLAND_ENTRY_2_RIDGE, OPTION_WEST, RIDGE_2_ISLAND_ENTRY, RIDGE, TimeInterval
        .advanceBy(10000)),
    RIDGE___CREVICE_WITH_CREEK(RIDGE, OPTION_NORTH, RIDGE_2_CREVICE_WITH_CREEK, OPTION_SOUTH, CREVICE_WITH_CREEK_2_RIDGE, CREVICE_WITH_CREEK, TimeInterval
        .advanceBy(10000)),
    CREVICE_WITH_CREEK___CREEK_UNDER_CLIFF(CREVICE_WITH_CREEK, OPTION_EAST, CREVICE_WITH_CREEK_2_CREEK_UNDER_CLIFF, OPTION_WEST, CREEK_UNDER_CLIFF_2_CREVICE_WITH_CREEK, CREEK_UNDER_CLIFF, TimeInterval
        .advanceBy(10000)),
    CREEK_UNDER_CLIFF___POND_OVERHANG(CREEK_UNDER_CLIFF, OPTION_EAST, CREEK_UNDER_CLIFF_2_POND_OVERHANG, OPTION_WEST, POND_OVERHANG_2_CREEK_UNDER_CLIFF, POND_OVERHANG, TimeInterval
        .advanceBy(10000)),
    POND_OVERHANG___DRY_CREEK(POND_OVERHANG, OPTION_EAST, POND_OVERHANG_2_DRY_CREEK, OPTION_WEST, DRY_CREEK_2_POND_OVERHANG, DRY_CREEK, TimeInterval
        .advanceBy(10000)),
    DRY_CREEK___CLIFF_MEADOW_EDGE_WEST(DRY_CREEK, OPTION_EAST, DRY_CREEK_2_CLIFF_MEADOW_EDGE_WEST, OPTION_WEST, CLIFF_MEADOW_EDGE_WEST_2_DRY_CREEK, CLIFF_MEADOW_EDGE_WEST, TimeInterval
        .advanceBy(10000)),
    CLIFF_MEADOW_EDGE_WEST___HOLE_IN_THE_GROUND(CLIFF_MEADOW_EDGE_WEST, OPTION_NORTH, CLIFF_MEADOW_EDGE_WEST_2_HOLE_IN_THE_GROUND, OPTION_SOUTH, HOLE_IN_THE_GROUND_2_CLIFF_MEADOW_EDGE_WEST, HOLE_IN_THE_GROUND, TimeInterval
        .advanceBy(10000)), ;
    
    public final Location loc1;
    public final Location loc2;
    public final Key loc1toloc2;
    public final TextKey loc1toloc2Description;
    public final Key loc2toloc1;
    public final TextKey loc2toloc1Description;
    public final TimeInterval timeInterval;
    
    // unidirectional path
    private Path(final Location loc1, final Key loc1toloc2, final TextKey loc1toloc2Description, final Location loc2,
        final TimeInterval timeInterval) {
      this(loc1, loc1toloc2, loc1toloc2Description, null, null, loc2, timeInterval);
    }
    
    // bidirectional path
    private Path(final Location loc1, final Key loc1toloc2, final TextKey loc1toloc2Description, final Key loc2toloc1,
        final TextKey loc2toloc1Description, final Location loc2, final TimeInterval timeInterval) {
      this.loc1 = loc1;
      this.loc2 = loc2;
      this.loc1toloc2 = loc1toloc2;
      this.loc1toloc2Description = loc1toloc2Description;
      this.loc2toloc1 = loc2toloc1;
      this.loc2toloc1Description = loc2toloc1Description;
      this.timeInterval = timeInterval;
    }
    
    private static final Logger PATH_LOGGER = LoggerFactory.getLogger(Location.class);
    
    public static void write(final @Nullable Path path, final @Nonnull ObjectOutput out) throws IOException {
      if (path == null) {
        out.writeObject(null); // input is null
      } else {
        out.writeObject(path.name());
      }
    }
    
    @CheckForNull
    public static Path read(final @Nonnull ObjectInput in) throws ClassNotFoundException, IOException {
      final String name = (String) in.readObject();
      if (name == null) {
        // input was null
        return null;
      }
      
      try {
        return valueOf(name);
      } catch (final RuntimeException e) {
        PATH_LOGGER.error("Failed to reconstruct Path " + name, e);
      }
      return null;
    }
  }
  
  private Location currentLocation;
  private Path currentPath;
  private Location toLocation;
  
  // for loading/saving
  public Map() {
    this(null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.currentLocation = Location.read(in);
    this.currentPath = Path.read(in);
    this.toLocation = Location.read(in);
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    Location.write(this.currentLocation, out);
    Path.write(this.currentPath, out);
    Location.write(this.toLocation, out);
  }
  
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
        if (State.is(path)) {
          if (path.loc1 == this.currentLocation) {
            if (path.loc1toloc2 != null && path.loc1toloc2Description != null) {
              addOption(path.loc1toloc2, path.loc1toloc2Description, new Map(path, path.loc2));
            }
          }
          if (path.loc2 == this.currentLocation) {
            if (path.loc2toloc1 != null && path.loc2toloc1Description != null) {
              addOption(path.loc2toloc1, path.loc2toloc1Description, new Map(path, path.loc1));
            }
          }
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
  public TimeInterval getRequiredTimeInterval() {
    if (this.currentPath != null) {
      return this.currentPath.timeInterval;
    } else {
      return super.getRequiredTimeInterval();
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    final List<ImageWrapper> images = new LinkedList<>();
    double centerX;
    double centerY;
    if (this.currentLocation != null) {
      centerX = this.currentLocation.x;
      centerY = this.currentLocation.y;
    } else {
      centerX = (this.currentPath.loc1.x + this.currentPath.loc2.x) / 2;
      centerY = (this.currentPath.loc1.y + this.currentPath.loc2.y) / 2;
      
    }
    images.add(getTile(centerX, centerY));
    addPaths(images, this.currentPath, centerX, centerY);
    addLocations(images, this.currentLocation, centerX, centerY);
    return ImgChange.setBGAndFGW(null, images.toArray(new ImageWrapper[images.size()]));
  }
  
  private static void addPaths(final List<ImageWrapper> images, final Path currentPath, final double centerX,
      final double centerY) {
    for (final Path path : Path.values()) {
      if (!is(path)) {
        continue; // path is not accessible to the player
      }
      final boolean isCurrent = path == currentPath;
      if (!isCurrent && (Math.abs(path.loc1.x - centerX) >= 0.5 || Math.abs(path.loc1.y - centerY) >= 0.5)
          && (Math.abs(path.loc2.x - centerX) >= 0.5 || Math.abs(path.loc2.y - centerY) >= 0.5)) {
        continue; // path not visible on current part of map
      }
      final ImageWrapper pathWrapper = new ImageWrapper(new ImageProxyPath(path.loc1.x, path.loc1.y, path.loc2.x,
          path.loc2.y, centerX, centerY, isCurrent));
      images.add(pathWrapper);
    }
  }
  
  private static void addLocations(final List<ImageWrapper> images, final Location currentLocation,
      final double centerX, final double centerY) {
    ImageWrapper current = null;
    for (final Location loc : Location.values()) {
      final boolean isCurrent = loc == currentLocation;
      if (Math.abs(loc.x - centerX) >= 0.5 || Math.abs(loc.y - centerY) >= 0.5) {
        continue; // location not visible on current part of map
      }
      ImageWrapper locWrapper = new ImageWrapper(loc);
      if (locWrapper.getResourceState() == ResourceState.MISSING) {
        locWrapper = new ImageWrapper(isCurrent ? MapIcons.CURRENT_LOCATION : MapIcons.NON_CURRENT_LOCATION);
      }
      final Properties placement = locWrapper.getPlacement();
      final double scaleX = Double.parseDouble(placement.getProperty(ProxyProps.SCALE_X.name()));
      final double scaleY = Double.parseDouble(placement.getProperty(ProxyProps.SCALE_Y.name()));
      final double xOffset = -scaleX / 2 + 50 + (loc.x - centerX) * 100;
      final double yOffset = -scaleY / 2 + 50 + (loc.y - centerY) * 100;
      placement.setProperty(ProxyProps.ALIGN_X.name(), "LEFT");
      placement.setProperty(ProxyProps.ALIGN_Y.name(), "TOP");
      placement.setProperty(ProxyProps.POS_X.name(), String.valueOf(xOffset));
      placement.setProperty(ProxyProps.POS_Y.name(), String.valueOf(yOffset));
      locWrapper.setPlacementWithoutWritingToDisk(placement);
      if (isCurrent) {
        current = locWrapper;
      } else {
        images.add(locWrapper);
      }
    }
    if (current != null) {
      images.add(current);
    }
  }
  
  private static ImageWrapper getTile(final double centerX, final double centerY) {
    final long tileX = Math.round(Math.floor(centerX));
    final long tileY = Math.round(Math.floor(centerY));
    final MapTiles tile = MapTiles.get(tileX, tileY);
    final ImageWrapper tileWrapper = new ImageWrapper(tile);
    tileWrapper.pinRandomImage();
    final Properties placement = new Properties();
    
    final double xOffset = -50 + (0.5 - (centerX - tileX)) * 100;
    final double yOffset = -50 + (0.5 - (centerY - tileY)) * 100;
    placement.setProperty(ProxyProps.ALIGN_X.name(), "LEFT");
    placement.setProperty(ProxyProps.ALIGN_Y.name(), "TOP");
    placement.setProperty(ProxyProps.SCALE_X.name(), "200");
    placement.setProperty(ProxyProps.SCALE_Y.name(), "200");
    placement.setProperty(ProxyProps.POS_X.name(), String.valueOf(xOffset));
    placement.setProperty(ProxyProps.POS_Y.name(), String.valueOf(yOffset));
    tileWrapper.setPlacementWithoutWritingToDisk(placement);
    return tileWrapper;
  }
}
