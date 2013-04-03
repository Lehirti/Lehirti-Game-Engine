package lge.events;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import lge.events.Event.NullState;
import lge.gui.EngineMain;
import lge.gui.Key;
import lge.res.ResourceCache;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextWrapper;
import lge.util.PathFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoadGameScreenEvent extends EventNode<NullState> {
  private int selectedItem;
  private final List<File> allSavegames;
  private final List<ImageKey> allImages = new LinkedList<>();
  
  private static final Logger LOGGER = LoggerFactory.getLogger(LoadGameScreenEvent.class);
  
  // for loading/saving
  public LoadGameScreenEvent() {
    this(0, PathFinder.getAllSavegames());
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.selectedItem = in.readInt();
    readPreviewFromSavegame();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(this.selectedItem);
  }
  
  public LoadGameScreenEvent(final int selectedItem, final List<File> allSavegames) {
    this.selectedItem = selectedItem;
    this.allSavegames = allSavegames;
    readPreviewFromSavegame();
  }
  
  private void readPreviewFromSavegame() {
    final File sav = this.allSavegames.get(this.selectedItem);
    
    try (FileInputStream fis = new FileInputStream(sav); ObjectInputStream ois = new ObjectInputStream(fis)) {
      // for load screen preview
      ois.readUTF();
      final int nrImages = ois.readInt();
      for (int i = 0; i < nrImages; i++) {
        final ImageKey key = ImageKey.IO.read(ois);
        if (key != null) {
          this.allImages.add(key);
        }
      }
      
      EngineMain.MAIN_WINDOW.repaint();
      
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " not found for loading", e);
    } catch (final IOException e) {
      LOGGER.error("IOException trying to load from " + sav.getAbsolutePath(), e);
    } catch (final ClassNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " incompatible with current program version.", e);
    }
  }
  
  private static String readNameFromSavegame(final File savegame) {
    try (FileInputStream fis = new FileInputStream(savegame); ObjectInputStream ois = new ObjectInputStream(fis)) {
      return ois.readUTF();
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + savegame.getAbsolutePath() + " not found for loading", e);
    } catch (final IOException e) {
      LOGGER.error("IOException trying to load from " + savegame.getAbsolutePath(), e);
    }
    return "[Savegame corrupt]";
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(null, this.allImages.toArray(new ImageKey[this.allImages.size()]));
  }
  
  @Override
  protected void doEvent() {
    setText(CommonText.SAVEGAMES);
    for (int i = 0; i < this.allSavegames.size(); i++) {
      final File save = this.allSavegames.get(i);
      addText(CommonText.NEWLINE);
      if (i == this.selectedItem) {
        addText(CommonText.MARKER);
        addOption(Key.OPTION_ENTER, CommonText.LOAD_SAVEGAME, new LoadGameEvent(save));
      }
      final TextWrapper tw = ResourceCache.get(CommonText.PARAMETER);
      tw.addParameter(readNameFromSavegame(save));
      addText(tw);
    }
    if (this.selectedItem != 0) {
      addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new LoadGameScreenEvent(this.selectedItem - 1,
          this.allSavegames));
    }
    if (this.selectedItem != this.allSavegames.size() - 1) {
      addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new LoadGameScreenEvent(this.selectedItem + 1,
          this.allSavegames));
    }
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new AltScreenToGameEvent());
  }
}
