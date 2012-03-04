package org.lehirti.res.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

import javax.imageio.ImageIO;

import org.lehirti.res.ResourceCache;
import org.lehirti.util.FileUtils;
import org.lehirti.util.Hash;

public class ImageProxy {
  private static final String RES = "res";
  private static final File CORE_RES_DIR = new File(ResourceCache.CORE_BASE_DIR, RES);
  private static final File MOD_RES_DIR = new File(ResourceCache.MOD_BASE_DIR, RES);
  
  static final String FILENAME_SUFFIX = ".proxy";
  
  private final File imageFile;
  private SoftReference<BufferedImage> image;
  
  private ImageProxy(final File imageProxyFile, final File imageFile, final BufferedImage image) {
    // TODO readImageProxyFile for scaling/alignment/etc
    
    this.imageFile = imageFile;
    this.image = new SoftReference<BufferedImage>(image);
  }
  
  static ImageProxy getInstance(final File imageProxyFile) {
    final int realImageFileNamelength = imageProxyFile.getName().length() - FILENAME_SUFFIX.length();
    File imageFile = new File(CORE_RES_DIR, imageProxyFile.getName().substring(0, realImageFileNamelength));
    if (!imageFile.exists()) {
      imageFile = new File(MOD_RES_DIR, imageProxyFile.getName().substring(0, realImageFileNamelength));
      if (!imageFile.exists()) {
        // TODO log missing image resource
        return null;
      }
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
  
  static ImageProxy createNew(final File modDir, final File alternativeImageFile) {
    if (alternativeImageFile.canRead()) {
      final String fileNameInResDir = Hash.calculateSHA1TODO(alternativeImageFile);
      if (!modDir.exists()) {
        if (!modDir.mkdirs()) {
          // TODO log failed to create dir
          return null;
        }
      }
      final String newFileName = fileNameInResDir + getExtension(alternativeImageFile);
      final File proxyFile = new File(modDir, newFileName + ImageProxy.FILENAME_SUFFIX);
      try {
        proxyFile.createNewFile();
      } catch (final IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      File resFile = new File(CORE_RES_DIR, newFileName);
      if (!resFile.exists()) {
        resFile = new File(MOD_RES_DIR, newFileName);
        if (!resFile.exists()) {
          FileUtils.copyFileTODO(alternativeImageFile, resFile);
        }
      }
      return getInstance(proxyFile);
    }
    return null;
  }
  
  private static String getExtension(final File fileName) {
    final String simpleName = fileName.getName();
    final int lastIndex = simpleName.lastIndexOf('.');
    if (lastIndex == -1) {
      return "";
    }
    return simpleName.substring(lastIndex);
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
