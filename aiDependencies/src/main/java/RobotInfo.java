package robotarena;

import javax.swing.ImageIcon;

public interface RobotInfo {
  public void reset();

  public void setPos(int x, int y);

  public String getName();
  public ImageIcon getImage();

  public int getDefX();
  public int getDefY();
  public double getDefHealth();

  public int getX();
  public int getY();
  public double getHealth();

  public RobotAI getAI();
  public RobotControl getControl();
}