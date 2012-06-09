package org.lehirti.luckysurvivor.npc.hinata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;
import org.lehirti.luckysurvivor.npc.AbstractNPC;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexToy;

public class Hinata extends AbstractNPC {
  private static final long serialVersionUID = 1L;
  
  public static enum Image implements ImageKey {
    MAIN,
    INDIFFERENT_TO_SEX_ACT;
  }
  
  public static enum Text implements TextKey {
    NAME,
    GENERAL_DESCRIPTION;
  }
  
  public static enum SexActImage implements ImageKey {
    // BEGIN GENERATED BLOCK SexAct
    FUCK_PUSSY,
    GET_PUSSY_FUCKED,
    INSERT_TOY_INTO_PUSSY,
    FUCK_ANAL,
    GET_FUCKED_ANALLY,
    GET_TITJOB,
    GIVE_TITJOB,
    GET_BLOWJOB,
    GIVE_BLOWJOB,
    // END GENERATED BLOCK SexAct
  }
  
  @Override
  public ImageKey getImage() {
    return Image.MAIN;
  }
  
  @Override
  public TextKey getName() {
    return Text.NAME;
  }
  
  @Override
  public Sex getSex() {
    return Sex.FEMALE;
  }
  
  @Override
  public List<TextWrapper> getGeneralDescription() {
    final List<TextWrapper> generalDescription = new LinkedList<TextWrapper>();
    generalDescription.add(ResourceCache.get(Text.GENERAL_DESCRIPTION));
    return generalDescription;
  }
  
  @Override
  public List<TextWrapper> getExamineDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getGoOgleDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getFlirtWithDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getGiveItemDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getInventoryDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getTalkToDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getSexActsDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public ImageKey getReactionImage(final SexAct act, final SexToy toy) {
    return Image.INDIFFERENT_TO_SEX_ACT;
  }
  
  @Override
  public List<TextWrapper> getReactionText(final SexAct act, final SexToy toy) {
    return Collections.emptyList();
  }
  
  @Override
  public ImageKey getSexActPerformedImage(final SexAct act, final SexToy toy) {
    return SexActImage.valueOf(act.name());
  }
  
  @Override
  public List<TextWrapper> getSexActPerformedText(final SexAct act, final SexToy toy) {
    return Collections.emptyList();
  }
  
  @Override
  public void updateStateAfterSexAct(final SexAct act, final SexToy toy) {
    // TODO Auto-generated method stub
  }
}
