package robotarena;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RobotArenaSettings {
  private final static boolean RANDOMROBOTSTARTPOS = false;

  // Available AI Implementations
  private final static RobotAI[] robotAIs = {
    new RobotAIDefault(),
    new RobotAIRandomMove(),
    new RobotAISeeker(),
    new RobotAIHitAndRunner(),
    new RobotAITagger()
  };

  // Limits
  private final static int MINWIDTH = 2;
  private final static int MAXWIDTH = 15;

  private final static int MINHEIGHT = 2;
  private final static int MAXHEIGHT = 15;

  private final static int MAXROBOTS = 10; // [TODO] find a better number

  private final static int ROBOTIMAGESIZE = 100;

  // Default Values
  private final static int DEFAULTWIDTH = 12;
  private final static int DEFAULTHEIGHT = 8;

  // Vars
  private static int arenaWidth;
  private static int arenaHeight;

  private static int robotCount;

  private static String failReason;

  private static boolean arenaSaved;

  // Public Variables
  // JPanel Controls
  // Arena Controls
  // SpinnerNumberModel(defaultValue, min, max, step);
  public static JSpinner widthSpinner;
  public static JSpinner heightSpinner;

  public static JSpinner robotAmountSpinner;

  // Robot Controls
  public static JTextArea robotName;
  public static JLabel robotImage;

  public static JSpinner posXSpinner;
  public static JSpinner posYSpinner;

  public static JComboBox aiList;
  public static JComboBox imageList;

  private static ArrayList<String> robotNames;
  private static ArrayList<String> robotImages;

  // Main Controls
  public static JTextArea logger;
  public static JButton btnStart;
  public static JButton btnStop;
  public static JButton btnRestart;

  public static void init() {
    arenaSaved = false;

    arenaWidth = 0;
    arenaHeight = 0;

    robotCount = 0;

    robotNames = new ArrayList<String>();
    robotImages = new ArrayList<String>();

    generateRobotNames();
    generateRobotImages();
    initArenaControls();
  }

  static void generateRobotNames() {
    robotNames.add("Greg");
    robotNames.add("Steven");
    robotNames.add("Max");
    robotNames.add("Peter");
    robotNames.add("Tyler");
    robotNames.add("Beth");
    robotNames.add("Ray");
    robotNames.add("Pembleton");
    robotNames.add("Robert");
    robotNames.add("Zach");
  }

  static void generateRobotImages() {
    robotImages.add("1554047213");
    robotImages.add("droid2");
    robotImages.add("bandit");
  }

  public static boolean addRobot(RobotInfo robot) {
    failReason = "";

    if (robot.getControl().getAllRobotsDictionary().containsKey(robot.getName())) {
      failReason += "A Robot with the name '"+ robot.getName() +"' already exists.\n";
    }
    if (robot.getDefX() < 0 || robot.getDefX() >= getArenaWidth()) {
      failReason += "Starting X was "+ robot.getDefX() +" but should be between 0 and "+ (getArenaWidth()-1) +".\n";
    }
    if (robot.getDefY() < 0 || robot.getDefY() >= getArenaHeight()) {
      failReason += "Starting Y was "+ robot.getDefX() +" but should be between 0 and "+ (getArenaHeight()-1) +".\n";
    }
    if(robot.getControl().isGridCellOccupied(robot.getDefX(), robot.getDefY()) != null) { 
      failReason += "A Robot is already at X:"+ robot.getDefX() +" Y:"+ robot.getDefY() +".\n";
    }

    if(failReason.length() > 0) {
      return false;
    } else {
      robot.getControl().registerRobot(robot);
      return true;
    }
  }

  public static void initArenaControls() {
    widthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTWIDTH, MINWIDTH, MAXWIDTH, 1));
    heightSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTHEIGHT, MINHEIGHT, MAXHEIGHT, 1));

    robotAmountSpinner = new JSpinner(new SpinnerNumberModel(2, 2, MAXROBOTS, 1));
  }

  public static void initRobotControls() {
    int startX = 0;
    int startY = 0;

    if(RANDOMROBOTSTARTPOS) {
      startX = (int)(Math.random() * arenaWidth);
      startY = (int)(Math.random() * arenaHeight);
    }
    initRobotControls(getRandomRobotName(), startX, startY, 0, getRandomRobotImageIndex());
  }

  public static void initRobotControls(String name, int x, int y, int robotAIIndex, int robotImageIndex) {
    robotName = new JTextArea(name, 0, 20);

    posXSpinner = new JSpinner(new SpinnerNumberModel(x, 0, getArenaWidth()-1, 1));
    posYSpinner = new JSpinner(new SpinnerNumberModel(y, 0, getArenaHeight()-1, 1));

    aiList = new JComboBox(robotAIs);
    aiList.setSelectedIndex(robotAIIndex);
    aiList.setSize(20, aiList.getPreferredSize().height);

    imageList = new JComboBox(robotImages.toArray(new String[0]));
    imageList.setSelectedIndex(robotImageIndex);
    imageList.setSize(20, imageList.getPreferredSize().height);

    robotImage = new JLabel();
    robotImage.setHorizontalAlignment(JLabel.CENTER);
    robotImage.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

    robotImage.setPreferredSize(new Dimension(ROBOTIMAGESIZE, ROBOTIMAGESIZE));
    updateRobotImage(robotImages.get(robotImageIndex));

    imageList.addActionListener((event) -> {
      String robotImageName = (String)imageList.getSelectedItem();
      updateRobotImage(robotImageName);
    });
  }

  public static void updateRobotImage(String robotImageName) {
    // Get Image instead of icon then fix the sizing
    Image img = new ImageIcon(RobotArenaSettings.class.getClassLoader().getResource(robotImageName + ".png")).getImage();
    ImageIcon icon = new ImageIcon(img.getScaledInstance(ROBOTIMAGESIZE, ROBOTIMAGESIZE, Image.SCALE_SMOOTH));

    robotImage.setIcon(icon);

    if (icon != null) {
      robotImage.setText(null);
    } else {
      robotImage.setText("Image Not Found");
    }
  }

  public static void log(String msg) {
    SwingUtilities.invokeLater(() -> {
      logger.append(msg +"\n");
    });
  }

  public static void logClear() {
    SwingUtilities.invokeLater(() -> {
      logger.setText("");
    });
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

  // Randomly get a robot name from the robotNames arraylist every time a new robot needs settings
  // If the list of names is empty then return blank.
  public static String getRandomRobotName() {
    if(robotNames.size() > 0) {
      int pos = (int)(Math.random() * robotNames.size());

      return robotNames.remove(pos);
    } else {
      return "";
    }
  }

  public static int getRandomRobotImageIndex() {
    if(robotImages.size() > 0) {
      return (int)(Math.random() * robotImages.size());
    } else {
      return 0;
    }
  }
}
