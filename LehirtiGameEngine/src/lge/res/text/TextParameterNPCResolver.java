package lge.res.text;

/**
 * Implementors of this interface must have a default constructor
 */
public interface TextParameterNPCResolver {
  
  public static final class Current {
    // TODO Load/Save
    private static TextParameterNPCResolver current;
    
    public static final TextParameterNPCResolver get() {
      return current;
    }
    
    public static final void set(final TextParameterNPCResolver resolver) {
      current = resolver;
    }
  }
  
  public String getParameterPrefix();
  
  public String resolveParameter(String parameter) throws TextParameterResolutionException;
}
