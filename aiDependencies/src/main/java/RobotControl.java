package robotarena;

import java.util.HashMap;
import java.util.Map;

// RobotControl
//   retrieves information, perform actions.
//   must interact with rest of code
//   move...() and fire() must return true if action is legal+carried out or false if it would have broken a rule(doesnt indicate a hit)

interface RobotControl {
  static HashMap<String, RobotInfo> robots = new HashMap<String, RobotInfo>();

  public RobotInfo getRobot();

  public boolean moveNorth();
  public boolean moveEast();
  public boolean moveSouth();
  public boolean moveWest();

  public boolean fire(int x, int y) throws InterruptedException;

  public boolean isMovePosLegal(int x, int y);
  public boolean isShotPosLegal(int x, int y);

  public RobotInfo isGridCellOccupied(int x, int y);

  public RobotInfo[] getAllRobots();
  public HashMap<String, RobotInfo> getAllRobotsDictionary();

  public void registerRobot(RobotInfo robotInfoIn);
}
