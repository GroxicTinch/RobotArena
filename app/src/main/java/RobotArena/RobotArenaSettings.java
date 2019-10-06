import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RobotArenaSettings {
  static boolean arenaSaved;

  // Limits
  final static int MINWIDTH = 8;
  final static int MAXWIDTH = 15;

  final static int MINHEIGHT = 8;
  final static int MAXHEIGHT = 15;

  final static int MAXROBOTS = 4; // [TODO] find a better number

  // Default Values
  final static int DEFAULTWIDTH = 12;
  final static int DEFAULTHEIGHT = 8;

  // JPanel Controls
  // Arena Controls
  // SpinnerNumberModel(defaultValue, min, max, step);
  public static JSpinner widthSpinner;
  public static JSpinner heightSpinner;

  public static JSpinner robotAmountSpinner;

  // Robot Controls
  public static JTextArea robotName;

  public static JSpinner posXSpinner;
  public static JSpinner posYSpinner;

  static ArrayList<String> robotNames;

  // Main Controls
  public static JTextArea logger;

  // Vars
  static int arenaWidth;
  static int arenaHeight;

  static int robotCount;

  static String failReason;

  static HashMap<String, Robot> robots;

  // [TODO] AI Type for each robot

  public static void init() {
    arenaSaved = false;

    arenaWidth = 0;
    arenaHeight = 0;

    robotCount = 0;

    robots = new HashMap<String, Robot>();
    robotNames = new ArrayList<String>();

    generateRobotNames();
    initArenaControls();
  }

  static void generateRobotNames() {
    robotNames.add("Greg");
    robotNames.add("Steven");
    robotNames.add("Max");
    robotNames.add("Peter");
  }

  public static void addLogMsg(String msg) {
    logger.append(msg +"\n");
  }

  public static boolean addRobot(Robot robot) {
    failReason = "";

    if (robots.containsKey(robot.getName())) {
      failReason += "A Robot with the name '"+ robot.getName() +"' already exists.\n";
    }
    if (robot.getStartX() < 0 || robot.getStartX() >= getArenaWidth()) {
      failReason += "Starting X was "+ robot.getStartX() +" but should be between 0 and "+ (getArenaWidth()-1) +".\n";
    }
    if (robot.getStartY() < 0 || robot.getStartY() >= getArenaHeight()) {
      failReason += "Starting Y was "+ robot.getStartX() +" but should be between 0 and "+ (getArenaHeight()-1) +".\n";
    }
    if(isGridCellOccupied(robot.getStartX(), robot.getStartY())) { 
      failReason += "A Robot is already at X:"+ robot.getStartX() +" Y:"+ robot.getStartX() +".\n";
    }

    if(failReason.length() > 0) {
      return false;
    } else {
      robots.put(robot.getName(), robot);
      return true;
    }
  }

  public static boolean isGridCellOccupied(int x, int y) {
    for(Map.Entry<String, Robot> entry : robots.entrySet()) {
      Robot robot = entry.getValue();
      if(robot.getX() == x && robot.getY() == y) {
        return true;
      }
    }
    return false;
  }

  public static void initArenaControls() {
    widthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTWIDTH, MINWIDTH, MAXWIDTH, 1));
    heightSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTHEIGHT, MINHEIGHT, MAXHEIGHT, 1));

    robotAmountSpinner = new JSpinner(new SpinnerNumberModel(2, 2, MAXROBOTS, 1));
  }

  public static void initRobotControls() {
    initRobotControls(getRobotName(), 0, 0);
  }

  public static void initRobotControls(String name, int x, int y) {
    robotName = new JTextArea(name, 0, 30);

    posXSpinner = new JSpinner(new SpinnerNumberModel(x, 0, getArenaWidth()-1, 1));
    posYSpinner = new JSpinner(new SpinnerNumberModel(y, 0, getArenaHeight()-1, 1));
  }

  public static void saveArenaSettings() {
    arenaSaved = true;

    arenaWidth = (Integer)widthSpinner.getValue();
    arenaHeight = (Integer)heightSpinner.getValue();
    robotCount = (Integer)robotAmountSpinner.getValue();

    widthSpinner = null;
    heightSpinner = null;
    robotAmountSpinner = null;
  }

  // Get Values
  public static int getArenaWidth() {
    if (!arenaSaved) {
      saveArenaSettings();
    }
    return arenaWidth;
  }

  public static int getArenaHeight() {
    if (!arenaSaved) {
      saveArenaSettings();
    }
    return arenaHeight;
  }

  public static String getFailReason() {
    return failReason;
  }

  public static int getRobotCount() {
    if (!arenaSaved) {
      saveArenaSettings();
    }
    return robotCount;
  }

  public static String getRobotName() {
    if(robotNames.size() > 0) {
      int pos = (int)(Math.random() * robotNames.size());

      return robotNames.remove(pos);
    } else {
      return "";
    }
  }

  public static HashMap<String, Robot> getRobots() {
    return robots;
  }
}
