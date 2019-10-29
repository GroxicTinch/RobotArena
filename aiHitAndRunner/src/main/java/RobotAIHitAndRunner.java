package robotarena;

import java.lang.*;

public class RobotAIHitAndRunner extends RobotAIListener {
  private final static long RUNTIMEINSECONDS = 5;

  Direction dir = Direction.NORTH;
  long runTimeout;

  public RobotAIHitAndRunner() {}

  @Override
  public String toString() {
    return "Hit and Runner";
  }

  /* Based on psudocode from David Cooper, SEC_2019s2_Assignment.pdf */
  public void runAI(RobotControl robotControl) throws InterruptedException {
    RobotInfo myRobot = robotControl.getRobot();
    runTimeout = 0;
    
    while(true) {
      int closestRobotDistance = -1;

      for(RobotInfo robot : robotControl.getAllRobots()) {
        if(robot.getName() != myRobot.getName() && robot.isAlive()) {
          int robotDistanceX = Math.abs(myRobot.getX() - robot.getX());
          int robotDistanceY = Math.abs(myRobot.getY() - robot.getY());

          int robotDistance = robotDistanceX + robotDistanceY;

          // NOTIFICATION READING
          synchronized(mutex) {
            NotificationMessage notification = notificationQueue.poll();

            if(notification != null) {
              if(notification.getMessage() == "hit") {
                runTimeout = System.currentTimeMillis() + (RUNTIMEINSECONDS * 1000);
              } else if(notification.getMessage() == "beenHit") {
                //[FIXME] Implement Something!!
              }
            }
          }

          if(closestRobotDistance == -1 || robotDistance < closestRobotDistance) {
            closestRobotDistance = robotDistance;

            if(robotDistanceX < robotDistanceY) {
              if(robot.getY() > myRobot.getY()) {
                setDir(Direction.SOUTH);
              } else {
                setDir(Direction.NORTH);
              }
            } else {
              if(robot.getX() > myRobot.getX()) {
                setDir(Direction.EAST);
              } else {
                setDir(Direction.WEST);
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

      sleepRand(1000);
    }
  }

  public void sleepRand(long time) throws InterruptedException {
    if(!RobotAI.AIEXACTTIMEBETWEENMOVES) {
      time += Math.random() * 400;
    }
    Thread.sleep(time);
  }

  public void setDir(Direction dirIn) {
    if(runTimeout <= System.currentTimeMillis()) {
      dir = dirIn;
    } else {
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
  }
}