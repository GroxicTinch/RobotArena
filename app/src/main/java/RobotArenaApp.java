package robotarena;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.*;

public class RobotArenaApp {
  public static void main(String[] args) {
    boolean userCanceled = false;
    // We want to ask the user before anything is initialized so do it before invoke.
    if(!askSettings()) {
      userCanceled = true;
    } else {
      RobotInfo robotInfo;

      // Settings need to be initialized before askRobotSettings is called
      RobotArenaSettings.initRobotControls();

      for(int i = 0; i < RobotArenaSettings.getRobotCount(); i++) {
        // If user cancels the GUI
        if(!askRobotSettings(i)) {
          userCanceled = true;
          break;
        } else {
          robot = new Robot(RobotArenaSettings.robotName.getText(),
                          (Integer)RobotArenaSettings.posXSpinner.getValue(),
                          (Integer)RobotArenaSettings.posYSpinner.getValue(),
                          (RobotAI)RobotArenaSettings.aiList.getSelectedItem());

          boolean robotAdded = RobotArenaSettings.addRobot(robot);

          if(!robotAdded) {
            // If the users robot was not added successfully then keep the settings of the Robot.
            i--;

            JOptionPane.showMessageDialog(null, RobotArenaSettings.getFailReason());
          } else if(i < (RobotArenaSettings.getRobotCount()-1)) {
            // If the users robot was added successfully then default the settings unless it was the last
            RobotArenaSettings.initRobotControls();
          }
        }
      }
    }

    // If user cancels the GUI
    if(!userCanceled) {
      SwingUtilities.invokeLater(() -> {
        JFrame window = new JFrame("RobotArena");
        SwingArena arena = new SwingArena();
        
        JToolBar toolbar = new JToolBar();
        JButton btnStartGame = new JButton("Start");
        JButton btnStopGame = new JButton("Stop");
        toolbar.add(btnStartGame);
        toolbar.add(btnStopGame);

        btnStartGame.setEnabled(true);
        btnStopGame.setEnabled(false);
        
        btnStartGame.addActionListener((event) -> {
          RobotArenaSettings.log("\n-- Game Started");

          btnStartGame.setEnabled(false);
          btnStopGame.setEnabled(true);
        });

        btnStopGame.addActionListener((event) -> {
          RobotArenaSettings.log("-- Game Stopped");

          btnStartGame.setEnabled(true);
          btnStopGame.setEnabled(false);
        });
        
        RobotArenaSettings.logger = new JTextArea();
        JScrollPane loggerArea = new JScrollPane(RobotArenaSettings.logger);
        loggerArea.setBorder(BorderFactory.createEtchedBorder());
        RobotArenaSettings.logger.setEditable(false);

        RobotArenaSettings.log("Grid initialized:\nwidth:"+ RobotArenaSettings.getArenaWidth() +"  height:"+ RobotArenaSettings.getArenaHeight());
        
        JSplitPane splitPane = new JSplitPane(
          JSplitPane.HORIZONTAL_SPLIT, arena, RobotArenaSettings.logger);

        Container contentPane = window.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(toolbar, BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(800, 800));
        window.pack();
        window.setVisible(true);
        
        splitPane.setDividerLocation(0.75);
      });
    } else {
      System.out.println("User Canceled");
    }
  }

  private static boolean askSettings() {
    RobotArenaSettings.init();

    int selection = JOptionPane.showConfirmDialog(null, arenaSettingsPanel(), "Robot Arena Settings"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (selection == JOptionPane.OK_OPTION) {
      try {
        RobotArenaSettings.widthSpinner.commitEdit();
        RobotArenaSettings.heightSpinner.commitEdit();
        RobotArenaSettings.robotAmountSpinner.commitEdit();
      } catch(ParseException ex) {
        // Do nothing
      }
      return true;
    } else /*if (selection == JOptionPane.CANCEL_OPTION)*/ {
      return false;
    }
  }

  private static boolean askRobotSettings(int robotNum) {
    int selection = JOptionPane.showConfirmDialog(null, robotSettingsPanel(), "Robot #"+ robotNum +" Settings"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (selection == JOptionPane.OK_OPTION) {
      try {
        RobotArenaSettings.posXSpinner.commitEdit();
        RobotArenaSettings.posYSpinner.commitEdit();
      } catch(ParseException ex) {
        // Do nothing
      }
      return true;
    } else /*if (selection == JOptionPane.CANCEL_OPTION)*/ {
      return false;
    }
  }

  private static JPanel arenaSettingsPanel() {
    JPanel basePanel = new JPanel();
    basePanel.setLayout(new GridLayout(3, 1, 5, 5));

    // Arena Size
    JPanel arenaSizePanel = new JPanel();
    TitledBorder arenaSizeTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Arena Size");
    arenaSizePanel.setBorder(arenaSizeTitle);
    arenaSizePanel.setLayout(new GridLayout(1, 5, 5, 5));

    arenaSizePanel.add(new JLabel("Width:"));
    arenaSizePanel.add(RobotArenaSettings.widthSpinner);
    arenaSizePanel.add(new JLabel()); // Blank cell

    arenaSizePanel.add(new JLabel("Height:"));
    arenaSizePanel.add(RobotArenaSettings.heightSpinner);
    basePanel.add(arenaSizePanel);

    // Robot Amount
    JPanel robotAmountPanel = new JPanel();
    TitledBorder robotTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Robots");
    robotAmountPanel.setBorder(robotTitle);
    robotAmountPanel.setLayout(new GridLayout(1, 5, 5, 5));

    robotAmountPanel.add(new JLabel("Amount:"));
    robotAmountPanel.add(RobotArenaSettings.robotAmountSpinner);
    robotAmountPanel.add(new JLabel()); // Blank cell
    robotAmountPanel.add(new JLabel()); // Blank cell
    robotAmountPanel.add(new JLabel()); // Blank cell

    basePanel.add(robotAmountPanel);

    return basePanel;
  }

  private static JPanel robotSettingsPanel() {
    JPanel basePanel = new JPanel();
    basePanel.setLayout(new GridLayout(3, 1, 5, 5));

    // Robot Name
    JPanel namePanel = new JPanel();
    TitledBorder nameTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Name");
    namePanel.setBorder(nameTitle);
    namePanel.add(RobotArenaSettings.robotName);
    basePanel.add(namePanel);

    // AI
    JPanel aiTypePanel = new JPanel();
    aiTypePanel.setLayout(new GridLayout(1, 1, 5, 5));
    TitledBorder aiTypeTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "AI Type");
    aiTypePanel.setBorder(aiTypeTitle);
    aiTypePanel.add(RobotArenaSettings.aiList);
    basePanel.add(aiTypePanel);

    // Arena Position
    JPanel arenaPosPanel = new JPanel();
    TitledBorder arenaPosTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Start Position");
    arenaPosPanel.setBorder(arenaPosTitle);
    arenaPosPanel.setLayout(new GridLayout(1, 5, 5, 5));

    arenaPosPanel.add(new JLabel("X:"));
    arenaPosPanel.add(RobotArenaSettings.posXSpinner);
    arenaPosPanel.add(new JLabel()); // Blank cell

    arenaPosPanel.add(new JLabel("Y:"));
    arenaPosPanel.add(RobotArenaSettings.posYSpinner);
    basePanel.add(arenaPosPanel);

    return basePanel;
  }
}
