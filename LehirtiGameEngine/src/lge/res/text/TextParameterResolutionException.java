package lge.res.text;

public final class TextParameterResolutionException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public TextParameterResolutionException(final String message) {
    super(message);
  }
  
  public TextParameterResolutionException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
