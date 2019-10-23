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

public class RobotAIRandomMove implements RobotAI {

  public RobotAIRandomMove() {}

  @Override
  public String toString() {
    return "Random Move Direction";
  }

  public void runAI(RobotControl robotControl) throws InterruptedException {
    RobotInfo myRobot = robotControl.getRobot();
    while(true) {
      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() &&
           robot.isAlive() &&
           Math.abs(myRobot.getX() - robot.getX()) <= 2 &&
           Math.abs(myRobot.getY() - robot.getY()) <= 2) {
          robotControl.fire(robot.getX(), robot.getY());
          break;
        }
      }

      // Choose a random direction, if that fails then go in the next direction, clockwise
      Direction dir = Direction.values()[(int)(Math.random() * 4)];
      boolean moved = false;

      while(!moved) {
        switch(dir) {
          case NORTH:
            if(robotControl.moveNorth()) {
              moved = true;
            } else {
              dir = Direction.EAST;
            }
            break;
          case EAST:
            if(robotControl.moveEast()) {
              moved = true;
            } else {
              dir = Direction.SOUTH;
            }
            break;
          case SOUTH:
            if(robotControl.moveSouth()) {
              moved = true;
            } else {
              dir = Direction.WEST;
            }
            break;
          case WEST:
            if(robotControl.moveWest()) {
              moved = true;
            } else {
              dir = Direction.NORTH;
            }
            break;
          default:
            break;
        }
      }

      Thread.sleep(1000);
    }
  }
}