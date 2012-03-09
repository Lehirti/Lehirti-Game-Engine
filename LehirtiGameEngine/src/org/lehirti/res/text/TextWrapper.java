package org.lehirti.res.text;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.res.ResourceCache;
import org.lehirti.util.FileUtils;

public class TextWrapper {
  
  private final TextKey key;
  private final File modFile;
  private String value;
  
  private final List<String> parameters = new LinkedList<String>();
  
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
  
  public String getRawValue() {
    return this.value;
  }
  
  public String getValue() {
    if (this.parameters.isEmpty()) {
      return getRawValue();
    }
    String val = this.value;
    for (int i = 0; i < this.parameters.size(); i++) {
      val = val.replaceAll("\\{" + i + "\\}", this.parameters.get(i));
    }
    return val;
  }
  
  public void setValue(final String value) {
    this.value = value;
    FileUtils.writeContentToFile(this.modFile, value);
  }
  
  public void addParameter(final String param) {
    this.parameters.add(param);
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
