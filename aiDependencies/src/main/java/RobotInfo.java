package robotarena;

import javax.swing.ImageIcon;

public class RobotInfo {
  String name;
  // Represents the image to draw.
  ImageIcon image;

  // Default variables
  int[] defPos;
  double defHealth;

  // Current variables
  int[] currPos;
  double currHealth;

  public RobotInfo(String nameIn, int xIn, int yIn, double healthIn, String imageName) {
    name = nameIn;

    defPos = new int[2];
    currPos = new int[2];

    defPos[0] = xIn;
    defPos[1] = yIn;

    defHealth = healthIn;

    // Here's how you get an Image object from an image file (which you provide in the 
    // 'resources/' directory.
    image = new ImageIcon(getClass().getClassLoader().getResource(imageName));
  }

  public void reset() {
    currPos[0] = defPos[0];
    currPos[1] = defPos[1];

    currHealth = defHealth;
  }

  public void setPos(int x, int y) {
    currPos[0] = x;
    currPos[1] = y;
  }

  public String getName() {return name;}
  public ImageIcon getImage() { return image; }

  public int getDefX() {return currPos[0];}
  public int getDefY() {return currPos[1];}
  public double getDefHealth() {return currHealth;}

  public int getX() {return currPos[0];}
  public int getY() {return currPos[1];}
  public double getHealth() {return currHealth;}
}