package robotarena;

import javax.swing.*;

// Virtual Robots
//   Controlled by one or more separate pieces of code(AI Implementations)
// $$  Use blocking queues for notifications
// $$    AIs get notified when they hit another or be hit by a robot
// $$      includes details of the other robot involved
// $$??    produce at least 1 more AI implementation to demonstrate how this feature works
// $$??    create another AI(or modify existing) to demonstrate this feature by having the robots react in some way

public class Robot {
  private static final String DEF_IMAGE_FILE = "1554047213.png";

  RobotInfo robotInfo;

  // TODO Add AI

  public Robot(String name, int x, int y) {
    this(name, x, y, 100.0, DEF_IMAGE_FILE);
  }

  public Robot(String name, int x, int y, String imageName) {
    this(name, x, y, 100.0, imageName);
  }

  public Robot(String name, int x, int y, double health, String imageName) {
    robotInfo = new RobotInfo(name, x, y, health, imageName);

    resetRobot();
  }

  public void resetRobot() {
    robotInfo.reset();
  }

  public RobotInfo getInfo() { return robotInfo; }
}