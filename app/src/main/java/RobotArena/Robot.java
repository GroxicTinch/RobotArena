import javax.swing.*;

public class Robot {
  private static final String DEF_IMAGE_FILE = "1554047213.png";

  String name;

  // Represents the image to draw.
  ImageIcon image;
  // TODO Add AI

  // Default Robot Settings
  int[] startPos;
  double startHealth;

  // Current Robot Settings
  int[] currPos;
  double currHealth;

  public Robot(String nameIn, int x, int y) {
    this(nameIn, x, y, 100.0, DEF_IMAGE_FILE);
  }

  public Robot(String nameIn, int x, int y, String imageName) {
    this(nameIn, x, y, 100.0, imageName);
  }

  public Robot(String nameIn, int x, int y, double health, String imageName) {
    startPos = new int[2];
    currPos = new int[2];

    name = nameIn;

    startPos[0] = x;
    startPos[1] = y;

    startHealth = health;

    // Here's how you get an Image object from an image file (which you provide in the 
    // 'resources/' directory.
    image = new ImageIcon(getClass().getClassLoader().getResource(imageName));

    resetRobot();
  }

  public void resetRobot() {
    currPos[0] = startPos[0];
    currPos[1] = startPos[1];

    currHealth = startHealth;
  }

  public String getName() { return name; }
  public ImageIcon getImage() { return image; }
  public int getStartX() { return startPos[0]; }
  public int getStartY() { return startPos[1]; }
  public int getX() { return currPos[0]; }
  public int getY() { return currPos[1]; }
  public double getHealth() { return currHealth; }

  public void setPos(int x, int y) {
    currPos[0] = x;
    currPos[1] = y;
  }

  public void setHealth(double health) {
    currHealth = health;
  }
}