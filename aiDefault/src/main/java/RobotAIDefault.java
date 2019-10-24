package robotarena;

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
      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() &&
           robot.isAlive() &&
           Math.abs(myRobot.getX() - robot.getX()) <= 2 &&
           Math.abs(myRobot.getY() - robot.getY()) <= 2) {
          robotControl.fire(robot.getX(), robot.getY());
          break;
        }
      }

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