package lge.res;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public enum ResourceState {
  LOADED,
  MISSING,
  MOD,
  CORE,
  DYNAMIC;
  
  public void write(final ObjectOutput out) throws IOException {
    out.writeUTF(name());
  }
  
  public static ResourceState read(final ObjectInput in) throws IOException {
    return valueOf(in.readUTF());
  }
}
