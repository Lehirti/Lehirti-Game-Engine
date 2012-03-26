package org.lehirti.res.text;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.util.FileUtils;
import org.lehirti.util.PathFinder;

public class TextWrapper implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private TextKey key;
  private String rawValue; // as stored on disc; may be different from what's displayed on screen
  
  private final List<String> parameters = new LinkedList<String>();
  
  // for saving/loading
  public TextWrapper() {
  }
  
  public TextWrapper(final TextKey key) {
    this.key = key;
    // for now, store text directly in the res dir
    final File modFile = PathFinder.getModFile(this.key);
    if (modFile.canRead()) {
      this.rawValue = FileUtils.readContentAsString(modFile);
    } else {
      final File coreFile = PathFinder.getCoreFile(this.key);
      if (coreFile.canRead()) {
        this.rawValue = FileUtils.readContentAsString(coreFile);
      } else {
        this.rawValue = key.getClass().getName() + "." + key.name() + ": No resource found!\n";
      }
    }
  }
  
  public String getRawValue() {
    return this.rawValue;
  }
  
  public String getValue() {
    if (this.parameters.isEmpty()) {
      return getRawValue();
    }
    String val = this.rawValue;
    for (int i = 0; i < this.parameters.size(); i++) {
      val = val.replaceAll("\\{" + i + "\\}", this.parameters.get(i));
    }
    return val;
  }
  
  public void setValue(final String value, final String contentDir) {
    this.rawValue = value;
    FileUtils.writeContentToFile(PathFinder.getModFile(this.key, contentDir), value);
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
    this.rawValue = (String) in.readObject();
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
    out.writeObject(this.rawValue);
    out.writeInt(this.parameters.size());
    for (final String param : this.parameters) {
      out.writeObject(param);
    }
  }
}
