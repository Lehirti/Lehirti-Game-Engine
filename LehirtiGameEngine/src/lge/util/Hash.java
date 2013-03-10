package lge.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hash {
  private static final Logger LOGGER = LoggerFactory.getLogger(Hash.class);
  
  public static String calculateSHA1(final File file) {
    final MessageDigest sha1;
    try {
      sha1 = MessageDigest.getInstance("SHA1");
    } catch (final NoSuchAlgorithmException e) {
      LOGGER.error("Hash algorithm SHA1 missing", e);
      return null;
    }
    
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    DigestInputStream dis = null;
    try {
      fis = new FileInputStream(file);
      bis = new BufferedInputStream(fis);
      dis = new DigestInputStream(bis, sha1);
      
      // update hash
      while (dis.read() != -1) {
      }
      
      return toHex(sha1.digest());
    } catch (final Exception e) {
      LOGGER.error("Unable to compute SHA1-hash for " + file.getAbsolutePath(), e);
      return null;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException e) {
          LOGGER.error("Unable to close file input stream for file " + file.getAbsolutePath(), e);
        }
      }
      if (bis != null) {
        try {
          bis.close();
        } catch (final IOException e) {
          LOGGER.error("Unable to close buffered input stream for file " + file.getAbsolutePath(), e);
        }
      }
      if (dis != null) {
        try {
          dis.close();
        } catch (final IOException e) {
          LOGGER.error("Unable to close digest input stream for file " + file.getAbsolutePath(), e);
        }
      }
    }
  }
  
  private static String toHex(final byte[] byteArray) {
    final Formatter formatter = new Formatter();
    for (final byte b : byteArray) {
      formatter.format("%02x", Byte.valueOf(b));
    }
    return formatter.toString();
  }
}
