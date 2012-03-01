package org.lehirit.state;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Player implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(final String name) {
    this.name = name;
  }
  
  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeLong(serialVersionUID);
    out.writeObject(this.name);
  }
  
  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    final long version = in.readLong();
    if (version == 1L) {
      readObject1(in);
    }
  }
  
  private void readObject1(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.name = (String) in.readObject();
  }
}
