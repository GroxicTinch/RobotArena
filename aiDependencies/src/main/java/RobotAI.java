package robotarena;

// AI classes
//   Implements RobotAI
//   must create two or more
//   Each is a different kind of AI
//   Must be two or more active in a game
//   Each instance controls a particular robot
//   Must run in its own thread
//   Uses RobotControl
//   Gets name, grid location and hp of all robots via RobotInfo

interface RobotAI {
  enum Direction { NORTH, SOUTH, EAST, WEST }

  public String toString();
  public void runAI(RobotControl robotControl) throws InterruptedException;
}
