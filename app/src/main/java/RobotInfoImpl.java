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
  private static final String DEF_IMAGE_FILE = "1554047213";
    
  private RobotAI robotAI;
  private RobotControl robotControl;
  private Thread thread;

  private String name;
  // Represents the image to draw.
  private ImageIcon image;

  // Default variables
  private int[] defPos;
  private double defHealth;

  // Current variables
  private int[] currPos;
  private double currHealth;

  private Object mutex = new Object();

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
    image = new ImageIcon(getClass().getClassLoader().getResource(imageName +".png"));
    robotAI = robotAIIn;
    robotControl = new RobotControlImpl(this);

    reset();
  }

  public void reset() {
    synchronized(mutex) {
      currPos[0] = defPos[0];
      currPos[1] = defPos[1];

      currHealth = defHealth;
    }
  }

  public void setPos(int x, int y) {
    synchronized(mutex) {
      currPos[0] = x;
      currPos[1] = y;
    }
  }

  public boolean damage(double amount) {
    boolean isDead = false;

    synchronized(mutex) {
      currHealth -= amount;

      if(!isAlive()) {
        currHealth = 0;
        isDead = true;
      }
    }

    return isDead;
  }

  public boolean isAlive() {
    synchronized(mutex) {
      return (currHealth > 0);
    }
  }

  public void setAndStartThread(Thread threadIn) {
    synchronized(mutex) {
      if(thread != null) {
        thread.interrupt();
      }

      thread = threadIn;
      thread.start();
    }
  }

  public void stopThread() {
    synchronized(mutex) {
      if(thread != null) {
        thread.interrupt();
        thread = null;
      }
    }
  }

  public Thread getThread() {synchronized(mutex) {return thread;}}

  public String getName() {return name;}
  public ImageIcon getImage() {return image;}

  public int getDefX() {return defPos[0];}
  public int getDefY() {return defPos[1];}
  public double getDefHealth() {return defHealth;}

  public int getX() {synchronized(mutex) {return currPos[0];}}
  public int getY() {synchronized(mutex) {return currPos[1];}}
  public double getHealth() {synchronized(mutex) {return currHealth;}}

  public RobotAI getAI() {return robotAI;}
  public RobotControl getControl() {return robotControl;}
}