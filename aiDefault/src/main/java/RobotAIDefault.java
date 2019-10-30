package robotarena;

public class RobotAIDefault implements RobotAI {

  public RobotAIDefault() {}

  @Override
  public String toString() {
    return "Default";
  }

  /* Based on psudocode from David Cooper, SEC_2019s2_Assignment.pdf */
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

      int i = 0;
      while(!moved && i < 4) {
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
        i++;
      }

      sleepRand(1000);
    }
  }

  public void sleepRand(long time) throws InterruptedException {
    if(!RobotAI.AIEXACTTIMEBETWEENMOVES) {
      time += Math.random() * 400;
    }
    Thread.sleep(time);
  }
}