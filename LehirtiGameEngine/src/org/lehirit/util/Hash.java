package org.lehirit.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;

public class Hash {
  public static String calculateSHA1TODO(final File file) {
    try {
      return calculateSHA1(file);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static String calculateSHA1(final File file) throws Exception {
    final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    final FileInputStream fis = new FileInputStream(file);
    final BufferedInputStream bis = new BufferedInputStream(fis);
    final DigestInputStream dis = new DigestInputStream(bis, sha1);
    
    // update hash
    while (dis.read() != -1) {
    }
    
    return toHex(sha1.digest());
  }
  
  private static String toHex(final byte[] byteArray) {
    final Formatter formatter = new Formatter();
    for (final byte b : byteArray) {
      formatter.format("%02x", b);
    }
    return formatter.toString();
  }
}
