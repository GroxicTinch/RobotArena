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
    return isMovePosLegal(robotInfo.getX(), robotInfo.getY() - 1);
  }

  public boolean moveEast() {
    return isMovePosLegal(robotInfo.getX() + 1, robotInfo.getY());
  }

  public boolean moveSouth() {
    return isMovePosLegal(robotInfo.getX(), robotInfo.getY() + 1);
  }

  public boolean moveWest() {
    return isMovePosLegal(robotInfo.getX() - 1, robotInfo.getY());
  }

  public boolean fire(int x, int y) {
    return true;
  }

  public boolean isMovePosLegal(int x, int y) {
    return (x >= 0 && x <= RobotArenaSettings.getArenaWidth() &&
            y >= 0 && y <= RobotArenaSettings.getArenaHeight() &&
            !isGridCellOccupied(x, y));
  }

  public boolean isShotPosLegal(int x, int y) {
    return (x >= 0 && x <= RobotArenaSettings.getArenaWidth() &&
            y >= 0 && y <= RobotArenaSettings.getArenaHeight());
  }

  public boolean isGridCellOccupied(int x, int y) {
    for(Map.Entry<String, RobotInfo> entry : robots.entrySet()) {
      RobotInfo robotInfo = entry.getValue();
      if(robotInfo.getX() == x && robotInfo.getY() == y) {
        return true;
      }
    }
    return false;
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
