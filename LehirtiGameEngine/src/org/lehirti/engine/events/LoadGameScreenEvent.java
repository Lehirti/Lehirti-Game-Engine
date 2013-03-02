package org.lehirti.engine.events;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.gui.Main;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.util.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoadGameScreenEvent extends EventNode<NullState> {
  private int selectedItem;
  private final List<File> allSavegames;
  private String nameOfSavegame;
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
      this.nameOfSavegame = ois.readUTF();
      final int nrImages = ois.readInt();
      for (int i = 0; i < nrImages; i++) {
        final ImageKey key = ImageKey.IO.read(ois);
        if (key != null) {
          this.allImages.add(key);
        }
      }
      
      Main.MAIN_WINDOW.repaint();
      
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " not found for loading", e);
    } catch (final IOException e) {
      LOGGER.error("IOException tryting to load from " + sav.getAbsolutePath(), e);
    } catch (final ClassNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " incompatible with current program version.", e);
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(null, this.allImages.toArray(new ImageKey[this.allImages.size()]));
  }
  
  @Override
  protected void doEvent() {
    setText(CommonText.SAVEGAMES);
    for (int i = 0; i < this.allSavegames.size(); i++) {
      addText(CommonText.NEWLINE);
      if (i == this.selectedItem) {
        addText(CommonText.MARKER);
        addOption(Key.OPTION_ENTER, CommonText.LOAD_SAVEGAME,
            new LoadGameEvent(this.allSavegames.get(this.selectedItem)));
      }
      final TextWrapper tw = ResourceCache.get(CommonText.PARAMETER);
      tw.addParameter(this.nameOfSavegame);
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
