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

public class RobotAIDefault implements RobotAI {

  public RobotAIDefault() {}

  @Override
  public String toString() {
    return "Default";
  }

  public void runAI(RobotControl robotControl) throws InterruptedException {
    Direction dir = Direction.NORTH;

    RobotInfo myRobot = robotControl.getRobot();
    while(true) {

      // TODO add delay for shooting
      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() &&
          Math.abs(myRobot.getX() - robot.getX()) <= 2 &&
          Math.abs(myRobot.getY() - robot.getY()) <= 2) {
          robotControl.fire(robot.getX(), robot.getY());
          break;
        }
      }

      switch(dir) {
        case NORTH:
          if(!robotControl.moveNorth()) { dir = Direction.EAST; }
          break;
        case EAST:
          if(!robotControl.moveNorth()) { dir = Direction.SOUTH; }
          break;
        case SOUTH:
          if(!robotControl.moveNorth()) { dir = Direction.WEST; }
          break;
        case WEST:
          if(!robotControl.moveNorth()) { dir = Direction.NORTH; }
          break;
        default:
          break;
      }

      Thread.sleep(1000);
    }
  }
}