package robotarena;

import javax.swing.ImageIcon;

// Virtual Robots
//   Controlled by one or more separate pieces of code(AI Implementations)
// $$  Use blocking queues for notifications
// $$    AIs get notified when they hit another or be hit by a robot
// $$      includes details of the other robot involved
// $$??    produce at least 1 more AI implementation to demonstrate how this feature works
// $$??    create another AI(or modify existing) to demonstrate this feature by having the robots react in some way


public class RobotInfoImpl implements RobotInfo {
  private static final String DEF_IMAGE_FILE = "1554047213.png";
    
  RobotAI robotAI;
  RobotControl robotControl;

  String name;
  // Represents the image to draw.
  ImageIcon image;

  // Default variables
  int[] defPos;
  double defHealth;

  // Current variables
  int[] currPos;
  double currHealth;

  public RobotInfoImpl(String nameIn, int xIn, int yIn, RobotAI robotAIIn) {
    this(nameIn, xIn, yIn, 100.0, DEF_IMAGE_FILE, robotAIIn);
  }

  public RobotInfoImpl(String nameIn, int xIn, int yIn, String imageName, RobotAI robotAIIn) {
    this(nameIn, xIn, yIn, 100.0, imageName, robotAIIn);
  }

  public RobotInfoImpl(String nameIn, int xIn, int yIn, double healthIn, String imageName, RobotAI robotAIIn) {
    name = nameIn;

    defPos = new int[2];
    currPos = new int[2];

    defPos[0] = xIn;
    defPos[1] = yIn;

    defHealth = healthIn;

    // Here's how you get an Image object from an image file (which you provide in the 
    // 'resources/' directory.
    image = new ImageIcon(getClass().getClassLoader().getResource(imageName));
    robotAI = robotAIIn;
    robotControl = new RobotControlImpl(this);

    reset();
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

  public RobotAI getAI() { return robotAI; }
  public RobotControl getControl() { return robotControl; }
}