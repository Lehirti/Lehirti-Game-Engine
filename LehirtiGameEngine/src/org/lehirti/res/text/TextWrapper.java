package org.lehirti.res.text;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.res.ResourceCache;
import org.lehirti.util.FileUtils;

public class TextWrapper implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private TextKey key;
  private File modFile;
  private String value;
  
  private final List<String> parameters = new LinkedList<String>();
  
  // for saving/loading
  public TextWrapper() {
  }
  
  public TextWrapper(final TextKey key) {
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
    return this.key.getClass().getSimpleName() + "." + this.key.name() + "[" + getValue() + "]";
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    final String className = (String) in.readObject();
    final String name = (String) in.readObject();
    this.key = (TextKey) Enum.valueOf((Class<? extends Enum>) Class.forName(className), name);
    this.modFile = new File(ResourceCache.MOD_RES_DIR, this.key.getClass().getName() + "." + this.key.name());
    this.value = (String) in.readObject();
    final int nrOfParams = in.readInt();
    this.parameters.clear();
    for (int i = 0; i < nrOfParams; i++) {
      this.parameters.add((String) in.readObject());
    }
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeObject(this.key.getClass().getName());
    out.writeObject(this.key.name());
    out.writeObject(this.value);
    out.writeInt(this.parameters.size());
    for (final String param : this.parameters) {
      out.writeObject(param);
    }
  }
}
