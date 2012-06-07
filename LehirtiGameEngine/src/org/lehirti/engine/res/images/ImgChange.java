package org.lehirti.engine.res.images;

import java.util.ArrayList;
import java.util.List;

public class ImgChange {
  private final boolean updateBackground;
  private final boolean clearForeground;
  private final ImageKey backgroundImage;
  private final List<ImageKey> addedFGImages = new ArrayList<ImageKey>(25);
  private final List<ImageKey> removedFGImages = new ArrayList<ImageKey>(25);
  
  private ImgChange(final boolean updateBackground, final boolean clearForeground, final ImageKey backgroundImage) {
    this.updateBackground = updateBackground;
    this.clearForeground = clearForeground;
    this.backgroundImage = backgroundImage;
  }
  
  public boolean isUpdateBackground() {
    return this.updateBackground;
  }
  
  public ImageKey getBackground() {
    return this.backgroundImage;
  }
  
  public boolean isClearForeground() {
    return this.clearForeground;
  }
  
  public List<ImageKey> getAddedFGImages() {
    return this.addedFGImages;
  }
  
  public List<ImageKey> getRemovedFGImages() {
    return this.removedFGImages;
  }
  
  private ImgChange addForeground(final ImageKey... foregroundImages) {
    for (final ImageKey img : foregroundImages) {
      if (img != null) {
        this.addedFGImages.add(img);
      }
    }
    return this;
  }
  
  /**
   * remove selected foreground images from screen
   * 
   * @param foregroundImagesToRemove
   */
  public ImgChange removeFG(final ImageKey... foregroundImagesToRemove) {
    for (final ImageKey img : foregroundImagesToRemove) {
      this.removedFGImages.add(img);
    }
    return this;
  }
  
  /**
   * clear background and foreground and set new images
   * 
   * @param backgroundImage
   * @param foregroundImages
   * @return
   */
  public static ImgChange setBGAndFG(final ImageKey backgroundImage, final ImageKey... foregroundImages) {
    return new ImgChange(true, true, backgroundImage).addForeground(foregroundImages);
  }
  
  /**
   * change background but leave foreground as-is
   * 
   * @param backgroundImage
   * @return
   */
  public static ImgChange setBG(final ImageKey backgroundImage) {
    return new ImgChange(true, false, backgroundImage);
  }
  
  /**
   * clear foreground and set new foreground images; leave background as-is
   * 
   * @param backgroundImage
   */
  public static ImgChange setFG(final ImageKey... foregroundImages) {
    return new ImgChange(false, true, null).addForeground(foregroundImages);
  }
  
  /**
   * add additional foreground images on top of existing images
   * 
   * @param foregroundImages
   */
  public static ImgChange addFG(final ImageKey... foregroundImages) {
    return new ImgChange(false, false, null).addForeground(foregroundImages);
  }
  
  /**
   * change nothing
   */
  public static ImgChange nullChange() {
    return new ImgChange(false, false, null);
  }
  
  /**
   * clear screen (remove background image and all foreground images
   */
  public static ImgChange clearScreen() {
    return new ImgChange(true, true, null);
  }
}
