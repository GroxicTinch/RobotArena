package robotarena;

import java.lang.*;

public class RobotAIHitAndRunner extends RobotAIDefault {
  private final static long RUNTIMEINSECONDS = 5;

  long runTimeout;

  public RobotAIHitAndRunner() {}

  @Override
  public String toString() {
    return "Hit and Runner";
  }

  /* Based on psudocode from David Cooper, SEC_2019s2_Assignment.pdf */
  public void runAI(RobotControl robotControl) throws InterruptedException {
    RobotInfo myRobot = robotControl.getRobot();

    Direction dir = Direction.NORTH;

    runTimeout = 0;
    
    while(true) {
      int closestRobotDistance = -1;

      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() && robot.isAlive()) {
          int robotDistanceX = Math.abs(myRobot.getX() - robot.getX());
          int robotDistanceY = Math.abs(myRobot.getY() - robot.getY());

          int robotDistance = robotDistanceX + robotDistanceY;

          // NOTIFICATION READING, does thread safety in RobotControl
          NotificationMessage notification = robotControl.getMsg();

          if(notification != null) {
            String msg = notification.getMessage();
            if(msg == "hit" || msg == "kill") {
              runTimeout = System.currentTimeMillis() + (RUNTIMEINSECONDS * 1000);
            }
          }

          if(closestRobotDistance == -1 || robotDistance < closestRobotDistance) {
            closestRobotDistance = robotDistance;

            if(robotDistanceX < robotDistanceY) {
              if(robot.getY() > myRobot.getY()) {
                dir = setDir(Direction.SOUTH);
              } else {
                dir = setDir(Direction.NORTH);
              }
            } else {
              if(robot.getX() > myRobot.getX()) {
                dir = setDir(Direction.EAST);
              } else {
                dir = setDir(Direction.WEST);
              }
            }
          }

          if(robotDistanceX <= 2 && robotDistanceY <= 2) {
            robotControl.fire(robot.getX(), robot.getY());
            break;
          }
        }
      }

      // Move away from the closest robot, if it bumps into something choose the next direction
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

  public Direction setDir(Direction dirIn) {
    Direction dir = dirIn;
    if(runTimeout > System.currentTimeMillis()) {
      switch(dirIn) {
        case NORTH:
          dir = Direction.SOUTH;
          break;
        case SOUTH:
          dir = Direction.SOUTH;
          break;
        case WEST:
          dir = Direction.EAST;
          break;
        case EAST:
          dir = Direction.WEST;
          break;
      }
    }

    return dir;
  }
}