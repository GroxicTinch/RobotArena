package robotarena;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class RobotControlImpl implements RobotControl {
  private static HashMap<String, RobotInfo> robots = new HashMap<String, RobotInfo>();
  private Object mutex = new Object();
  private BlockingQueue<NotificationMessage> notificationQueue;

  public RobotInfo robotInfo;

  public RobotControlImpl() {}

  public RobotControlImpl(RobotInfo robotInfoIn) {
    robotInfo = robotInfoIn;
    notificationQueue = new ArrayBlockingQueue<NotificationMessage>(10);
  }

  public RobotInfo getRobot() {
    return robotInfo;
  }

  public boolean moveNorth() {
    return move(robotInfo.getX(), robotInfo.getY() - 1);
  }

  public boolean moveEast() {
    return move(robotInfo.getX() + 1, robotInfo.getY());
  }

  public boolean moveSouth() {
    return move(robotInfo.getX(), robotInfo.getY() + 1);
  }

  public boolean moveWest() {
    return move(robotInfo.getX() - 1, robotInfo.getY());
  }

  private boolean move(int x, int y) {
    boolean moveLegal = isMovePosLegal(x, y);
    if(moveLegal) {
      SwingArena.getInstance().setRobotPosition(robotInfo, x, y);
    }
    return moveLegal;
  }

  public boolean fire(int x, int y) throws InterruptedException {
    boolean shotLegal = isShotPosLegal(x, y);
    if(shotLegal) {
      Thread.sleep(500);
      SwingArena.getInstance().shoot(robotInfo, x, y);
    }
    return true;
  }

  public void tell(NotificationMessage notificationMessage) {
    synchronized(mutex) {
      if(notificationQueue.size() == 10) {
        /* Make sure that if its running slow it doesnt just ignore new messages
          itll instead drop its oldest one before adding a new one.
        */
        notificationQueue.poll(); 
      }
      notificationQueue.offer(notificationMessage);
    }
  }

  public NotificationMessage getMsg() {
    synchronized(mutex) {
      return notificationQueue.poll();
    }
  }

  public boolean isMovePosLegal(int x, int y) {
    return (x >= 0 && x < RobotArenaSettings.getArenaWidth() &&
            y >= 0 && y < RobotArenaSettings.getArenaHeight() &&
            isGridCellOccupied(x, y) == null);
  }

  public boolean isShotPosLegal(int x, int y) {
    return (x >= 0 && x < RobotArenaSettings.getArenaWidth() &&
            y >= 0 && y < RobotArenaSettings.getArenaHeight());
  }

  public RobotInfo isGridCellOccupied(int x, int y) {
    for(Map.Entry<String, RobotInfo> entry : robots.entrySet()) {
      RobotInfo robotInfo = entry.getValue();
      if(robotInfo.getX() == x && robotInfo.getY() == y) {
        return robotInfo;
      }
    }
    return null;
  }

  public RobotInfo[] getAllRobots() {
    return robots.values().toArray(new RobotInfo[robots.size()]);
  }

  public HashMap<String, RobotInfo> getAllRobotsDictionary() {
    return robots;
  }

  public void registerRobot(RobotInfo robotInfoIn) {
    robots.put(robotInfoIn.getName() ,robotInfoIn);
  }
}
