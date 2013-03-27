package net.cloudengine.widgets.panel;

import javaforce.voip.SIPClient;
import net.cloudengine.widgets.sound.PhoneLine;

import org.eclipse.swt.graphics.Image;

public interface GUI {
  
  public void selectLine(int newline);
  public void updateLine();
  public void updateCallButton(boolean state);
  public void updateEndButton(boolean state);
  
  public void endLineUpdate(int xline);
  public void endLineUpdate(PhoneLine line);
  
  public void callInviteUpdate();
  
//  public void setStatus(String number, String server, String status);
  
  public void hld_setIcon(Image image);
  public void aa_setIcon(Image image);
  public void ac_setIcon(Image image);
  public void dnd_setIcon(Image image);
  public void cnf_setIcon(Image image);
  public void mute_setIcon(Image image);
  public void spk_setIcon(Image image);
  public void onRegister(SIPClient sip);
//  public void updateRecentList();
}
