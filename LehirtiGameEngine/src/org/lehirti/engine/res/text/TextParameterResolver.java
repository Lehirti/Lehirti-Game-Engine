package org.lehirti.engine.res.text;

/**
 * Implementors of this interface must have a default constructor
 */
public interface TextParameterResolver {
  public static final class Current {
    // TODO Load/Save
    private static TextParameterResolver current;
    
    public static final TextParameterResolver get() {
      return current;
    }
    
    public static final void set(final TextParameterResolver resolver) {
      current = resolver;
    }
  }
  
  public String getParameterPrefix();
  
  public String resolveParameter(String parameterSuffix) throws TextParameterResolutionException;
}
