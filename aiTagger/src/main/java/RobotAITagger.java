package robotarena;

import java.lang.*;

public class RobotAITagger extends RobotAIDefault {
  private static boolean doOnce = true;
  
  public RobotAITagger() {}

  @Override
  public String toString() {
    return "Tagger";
  }

  /* Based on psudocode from David Cooper, SEC_2019s2_Assignment.pdf */
  public void runAI(RobotControl robotControl) throws InterruptedException {
    RobotInfo myRobot = robotControl.getRobot();

    boolean imIt;

    if(doOnce) {
      imIt = true;
      doOnce = false;
    } else {
      imIt = false;
    }

    while(true) {
      // NOTIFICATION READING, does thread safety in RobotControl
      NotificationMessage notification = robotControl.getMsg();

      if(notification != null) {
        if(notification.getRobot().getAI() instanceof RobotAITagger){
          // We dont care about kills because that would mean the only person that is "it" is dead
          // (still can happen if theres a different AI out there)
          String msg = notification.getMessage();
          if(msg == "hit") {
            imIt = false;
          } else if(msg == "beenHit") {
            imIt = true;
          }
        }
      }

      if(imIt) {
        for(RobotInfo robot : robotControl.getAllRobots()) {
          if(robot.getName() != myRobot.getName() &&
             robot.isAlive() &&
             Math.abs(myRobot.getX() - robot.getX()) <= 2 &&
             Math.abs(myRobot.getY() - robot.getY()) <= 2) {
            robotControl.fire(robot.getX(), robot.getY());
            break;
          }
        }
      }

      // Choose a random direction, if that fails then go in the next direction, clockwise
      Direction dir = Direction.values()[(int)(Math.random() * 4)];
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
}