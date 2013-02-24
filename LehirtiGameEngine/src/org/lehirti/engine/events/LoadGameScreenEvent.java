package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.util.PathFinder;

public final class LoadGameScreenEvent extends EventNode<NullState> implements Externalizable {
  private int selectedItem;
  private final List<File> allSavegames;
  
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
    // TODO read data from selected savegame
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange(); // TODO
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
      tw.addParameter(String.valueOf(i)); // TODO
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
