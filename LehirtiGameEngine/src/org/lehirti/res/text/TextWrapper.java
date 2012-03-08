package org.lehirti.res.text;

import java.io.File;

import org.lehirti.res.ResourceCache;
import org.lehirti.util.FileUtils;

public class TextWrapper {
  
  private final TextKey key;
  private final File modFile;
  private String value;
  
  public TextWrapper(final TextKey key, final File coreDir, final File modDir) {
    this.key = key;
    // for now, store text directly in the res dir
    this.modFile = new File(ResourceCache.MOD_RES_DIR, key.getClass().getName() + "." + key.name());
    if (this.modFile.canRead()) {
      this.value = FileUtils.readContentAsString(this.modFile);
    } else {
      final File coreFile = new File(ResourceCache.CORE_RES_DIR, key.getClass().getName() + "." + key.name());
      if (coreFile.canRead()) {
        this.value = FileUtils.readContentAsString(coreFile);
      } else {
        this.value = key.getClass().getName() + "." + key.name() + ": No resource found!";
      }
    }
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(final String value) {
    this.value = value;
    FileUtils.writeContentToFile(this.modFile, value);
  }
  
  @Override
  public int hashCode() {
    return this.key.hashCode();
  }
  
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof TextWrapper)) {
      return false;
    }
    final TextWrapper other = (TextWrapper) o;
    return this.key.equals(other.key);
  }
  
  @Override
  public String toString() {
    return this.key.getClass().getSimpleName() + "." + this.key.name() + "[" + this.value + "]";
  }
}
