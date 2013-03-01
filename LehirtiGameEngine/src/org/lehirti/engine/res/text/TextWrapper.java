package org.lehirti.engine.res.text;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.ResourceState;
import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.ObjState;
import org.lehirti.engine.state.State;
import org.lehirti.engine.state.StringState;
import org.lehirti.engine.util.FileUtils;
import org.lehirti.engine.util.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextWrapper implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TextWrapper.class);
  
  private TextKey key;
  private String rawValue; // as stored on disc; may be different from what's displayed on screen
  
  private final List<Object> parameters = new LinkedList<>();
  
  private final ResourceState state;
  
  // for saving/loading
  public TextWrapper() {
    this.state = ResourceState.LOADED;
  }
  
  public TextWrapper(final TextKey key, final boolean logModCoreDiff) {
    this.key = key;
    // for now, store text directly in the res dir
    final File modFile = PathFinder.getModFile(this.key);
    if (logModCoreDiff) {
      if (modFile.canRead()) {
        System.out.println(this.key.getClass().getName() + "." + this.key.name());
        this.rawValue = FileUtils.readContentAsString(modFile);
        this.state = ResourceState.MOD;
        final File coreFile = PathFinder.getCoreFile(this.key);
        if (coreFile.canRead()) {
          final String coreRawValue = FileUtils.readContentAsString(coreFile);
          System.out.println("CORE: " + coreRawValue.replaceAll("\\n", "\\\\n"));
          System.out.println("MOD:  " + this.rawValue.replaceAll("\\n", "\\\\n"));
          final int length = coreRawValue.length() < this.rawValue.length() ? coreRawValue.length() : this.rawValue
              .length();
          final StringBuilder sb = new StringBuilder();
          for (int i = 0; i < length; i++) {
            if (coreRawValue.charAt(i) == this.rawValue.charAt(i)) {
              sb.append(' ');
            } else {
              sb.append('*');
            }
          }
          final int lengthDiff = Math.abs(this.rawValue.length() - coreRawValue.length());
          for (int i = 0; i < lengthDiff; i++) {
            sb.append('+');
          }
          if (sb.toString().trim().length() == 0) {
            System.out.println("NO DIFF");
          } else {
            System.out.println("DIFF: " + sb.toString());
          }
        } else {
          System.out.println("NEW:  " + this.rawValue.replaceAll("\\n", "\\\\n"));
        }
        System.out.println();
      } else {
        final File coreFile = PathFinder.getCoreFile(this.key);
        if (coreFile.canRead()) {
          this.rawValue = FileUtils.readContentAsString(coreFile);
          this.state = ResourceState.CORE;
        } else {
          this.rawValue = "Ctrl-t: " + key.getClass().getSimpleName() + "." + key.name() + "\n\n";
          this.state = ResourceState.MISSING;
        }
      }
    } else {
      if (modFile.canRead()) {
        this.rawValue = FileUtils.readContentAsString(modFile);
        this.state = ResourceState.MOD;
      } else {
        final File coreFile = PathFinder.getCoreFile(this.key);
        if (coreFile.canRead()) {
          this.rawValue = FileUtils.readContentAsString(coreFile);
          this.state = ResourceState.CORE;
        } else {
          this.rawValue = "Ctrl-t: " + key.getClass().getSimpleName() + "." + key.name() + "\n\n";
          this.state = ResourceState.MISSING;
        }
      }
    }
  }
  
  public String getRawValue() {
    return this.rawValue;
  }
  
  public ResourceState getResourceState() {
    return this.state;
  }
  
  public String getValue() {
    String val = this.rawValue;
    int fromIndex = 0;
    while ((fromIndex = val.indexOf("{", fromIndex)) != -1) {
      final int toIndex = val.indexOf("}", fromIndex + 1);
      final String parameter = val.substring(fromIndex + 1, toIndex);
      final String replacement = getParameterReplacement(parameter);
      LOGGER.debug("Parameter \"{}\" found in \"{}\"", parameter, val);
      val = val.replace("{" + parameter + "}", replacement);
    }
    return val;
  }
  
  private String getParameterReplacement(final String parameter) {
    // try explicitly set parameters
    try {
      final int parsedInt = Integer.parseInt(parameter);
      if (parsedInt >= 0 && parsedInt < this.parameters.size()) {
        final Object param = this.parameters.get(parsedInt);
        if (param instanceof String) {
          return (String) param;
        } else {
          return ((TextWrapper) param).getValue();
        }
      } else {
        LOGGER.error("{} is missing explicitly set parameter #{}", toString(), Integer.valueOf(parsedInt));
        return "[MISSING PARAMETER #" + parameter + "]";
      }
    } catch (final NumberFormatException ignore) {
      // is not an explicitly set parameter; go on trying other possibilities
    }
    
    // try expanded FQCN
    final int endOfClassname = parameter.lastIndexOf(".");
    if (endOfClassname != -1) {
      final String className = parameter.substring(0, endOfClassname);
      final String name = parameter.substring(endOfClassname + 1);
      try {
        final Enum parameterValue = Enum.valueOf((Class<? extends Enum>) Class.forName(className), name);
        if (parameterValue instanceof TextKey) {
          return ResourceCache.get((TextKey) parameterValue).getValue();
        } else if (parameterValue instanceof StringState) {
          return State.get((StringState) parameterValue);
        } else if (parameterValue instanceof IntState) {
          return String.valueOf(State.get((IntState) parameterValue));
        } else if (parameterValue instanceof ObjState) {
          return State.get((ObjState) parameterValue).toString();
        } else if (parameterValue instanceof BoolState) {
          return String.valueOf(State.is((BoolState) parameterValue));
        }
      } catch (final ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    LOGGER.error("Unable to resolve parameter \"{}\" for {}", parameter, toString());
    return "[CANNOT RESOLVE PARAMETER \"" + parameter + "\"]";
  }
  
  public void setValue(final String value, final String contentDir) {
    this.rawValue = value;
    FileUtils.writeContentToFile(PathFinder.getModFile(this.key, contentDir), value);
  }
  
  public void addParameter(final String param) {
    this.parameters.add(param);
  }
  
  public void addParameter(final TextWrapper param) {
    this.parameters.add(param);
  }
  
  /**
   * @return this TextWrapper and all (recursively) contained (as parameters) TextWrappers
   */
  public List<TextWrapper> getAllTexts() {
    final List<TextWrapper> allTexts = new LinkedList<>();
    allTexts.add(this);
    for (final Object param : this.parameters) {
      if (param instanceof TextWrapper) {
        allTexts.addAll(((TextWrapper) param).getAllTexts());
      }
    }
    return allTexts;
  }
  
  public TextKey getTextKey() {
    return this.key;
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
    return this.key.getClass().getSimpleName() + "." + this.key.name() + "[" + this.rawValue + "]";
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
      this.parameters.add(in.readObject());
    }
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeObject(this.key.getClass().getName());
    out.writeObject(this.key.name());
    out.writeObject(this.rawValue);
    out.writeInt(this.parameters.size());
    for (final Object param : this.parameters) {
      out.writeObject(param);
    }
  }
}
