package robotarena;

import java.util.HashMap;
import java.util.Map;

// RobotControl
//   retrieves information, perform actions.
//   must interact with rest of code
//   move...() and fire() must return true if action is legal+carried out or false if it would have broken a rule(doesnt indicate a hit)

public class RobotControlImpl implements RobotControl {
  static HashMap<String, RobotInfo> robots = new HashMap<String, RobotInfo>();

  public RobotInfo robotInfo;

  public RobotControlImpl() {}

  public RobotControlImpl(RobotInfo robotInfoIn) {
    robotInfo = robotInfoIn;
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
