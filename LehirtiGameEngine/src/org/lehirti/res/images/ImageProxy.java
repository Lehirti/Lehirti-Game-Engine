package org.lehirti.res.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

import javax.imageio.ImageIO;

public class ImageProxy {
  private final static File RES_DIR = new File("res");
  
  static final String FILENAME_SUFFIX = ".proxy";
  
  private final File imageFile;
  private SoftReference<BufferedImage> image;
  
  ImageProxy(final ImageKey key) {
    // TODO Auto-generated constructor stub
    this.imageFile = null;
    this.image = null;
  }
  
  private ImageProxy(final File imageProxyFile, final File imageFile, final BufferedImage image) {
    // TODO readImageProxyFile for scaling/alignment/etc
    
    this.imageFile = imageFile;
    this.image = new SoftReference<BufferedImage>(image);
  }
  
  static ImageProxy getInstance(final File imageProxyFile) {
    final int realImageFileNamelength = imageProxyFile.getName().length() - FILENAME_SUFFIX.length();
    final File imageFile = new File(RES_DIR, imageProxyFile.getName().substring(0, realImageFileNamelength));
    if (!imageFile.exists()) {
      // TODO log missing image resource
      return null;
    }
    if (!imageFile.canRead()) {
      // TODO log permission problems
      return null;
    }
    try {
      final BufferedImage image = ImageIO.read(imageFile);
      return new ImageProxy(imageProxyFile, imageFile, image);
    } catch (final IOException ex) {
      // log file is not a valid image
      return null;
    }
  }
  
  public BufferedImage getImage() {
    BufferedImage bufferedImage = this.image.get();
    if (bufferedImage == null) {
      try {
        bufferedImage = ImageIO.read(this.imageFile);
        this.image = new SoftReference<BufferedImage>(bufferedImage);
      } catch (final IOException e) {
        // reading image used to work and now it doesn't anymore?
        // TODO return dummy image; log error
        e.printStackTrace();
      }
    }
    return bufferedImage;
  }
}
