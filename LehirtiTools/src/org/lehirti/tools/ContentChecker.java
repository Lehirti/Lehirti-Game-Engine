package org.lehirti.tools;

import java.util.Vector;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.ResourceState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImageWrapper;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.util.ClassFinder;
import org.lehirti.engine.util.PathFinder;
import org.lehirti.luckysurvivor.C;

public final class ContentChecker {
  public static void main(final String[] args) {
    for (final C content : C.values()) {
      PathFinder.registerContentDir(content.name());
    }
    
    int iCore = 0;
    int iMod = 0;
    final Vector<Class<?>> imageEnums = new ClassFinder().findSubclasses(ImageKey.class.getName());
    for (final Class<?> imageEnum : imageEnums) {
      final ImageKey[] imageKeys = (ImageKey[]) imageEnum.getEnumConstants();
      if (imageKeys != null) {
        for (final ImageKey key : imageKeys) {
          final ImageWrapper imageWrapper = ResourceCache.get(key);
          if (imageWrapper.getResourceState() == ResourceState.MISSING) {
            System.out.println(ResourceState.MISSING + " image: " + imageWrapper.toString());
          } else {
            iCore += imageWrapper.getNrOfCoreImages();
            iMod += imageWrapper.getNrOfModImages();
          }
        }
      }
    }
    
    System.out.println();
    
    int tCore = 0;
    int tMod = 0;
    final Vector<Class<?>> textEnums = new ClassFinder().findSubclasses(TextKey.class.getName());
    for (final Class<?> textEnum : textEnums) {
      final TextKey[] textKeys = (TextKey[]) textEnum.getEnumConstants();
      if (textKeys != null) {
        for (final TextKey key : textKeys) {
          final TextWrapper textWrapper = ResourceCache.get(key);
          if (textWrapper.getResourceState() == ResourceState.MISSING) {
            System.out.println(ResourceState.MISSING + " text: " + key.getClass().getName() + "." + key.name());
          } else if (textWrapper.getResourceState() == ResourceState.CORE) {
            tCore++;
          } else if (textWrapper.getResourceState() == ResourceState.MOD) {
            tMod++;
          }
        }
      }
    }
    
    System.out.println();
    System.out.println(iCore + " core images.");
    System.out.println(iMod + " mod images.");
    System.out.println(tCore + " core texts.");
    System.out.println(tMod + " mod texts.");
    System.out.println("DONE.");
  }
}
