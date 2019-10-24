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

public class RobotAISeeker implements RobotAI {

  public RobotAISeeker() {}

  @Override
  public String toString() {
    return "Seeker";
  }

  public void runAI(RobotControl robotControl) throws InterruptedException {
    RobotInfo myRobot = robotControl.getRobot();
    Direction dir = Direction.NORTH;

    while(true) {
      int closestRobotDistance = -1;

      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() && robot.isAlive()) {
          int robotDistanceX = Math.abs(myRobot.getX() - robot.getX());
          int robotDistanceY = Math.abs(myRobot.getY() - robot.getY());

          int robotDistance = robotDistanceX + robotDistanceY;

          if(closestRobotDistance == -1 || robotDistance < closestRobotDistance) {
            closestRobotDistance = robotDistance;

            // If the robot in columns than in rows then move vertically
            if(robotDistanceX < robotDistanceY) {
              if(robot.getY() < myRobot.getY()) {
                dir = Direction.NORTH;
              } else {
                dir = Direction.SOUTH;
              }
            } else {
              if(robot.getX() < myRobot.getX()) {
                dir = Direction.WEST;
              } else {
                dir = Direction.EAST;
              }
            }
          }

          if(robotDistanceX <= 2 && robotDistanceY <= 2) {
            robotControl.fire(robot.getX(), robot.getY());
            break;
          }
        }
      }

      // Move towards the closest robot, if it cannot move that direction then choose the next direction.
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