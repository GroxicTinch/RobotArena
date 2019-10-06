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
      Robot robot;
      RobotArenaSettings.initRobotControls();

      for(int i = 0; i < RobotArenaSettings.getRobotCount(); i++) {
        // If user cancels the GUI
        if(!askRobotSettings(i)) {
          userCanceled = true;
          break;
        } else {
          robot = new Robot(RobotArenaSettings.robotName.getText(),
                          (Integer)RobotArenaSettings.posXSpinner.getValue(),
                          (Integer)RobotArenaSettings.posYSpinner.getValue());

          boolean robotAdded = RobotArenaSettings.addRobot(robot);

          if(!robotAdded) {
            RobotArenaSettings.initRobotControls(robot.getName(), robot.getX(), robot.getY());
            i--;

            JOptionPane.showMessageDialog(null, RobotArenaSettings.getFailReason());
          } else {
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
        JButton btn1 = new JButton("My Button 1");
        JButton btn2 = new JButton("My Button 2");
        toolbar.add(btn1);
        toolbar.add(btn2);
        
        btn1.addActionListener((event) -> {
          System.out.println("Button 1 pressed");
        });
        
        RobotArenaSettings.logger = new JTextArea();
        JScrollPane loggerArea = new JScrollPane(RobotArenaSettings.logger);
        loggerArea.setBorder(BorderFactory.createEtchedBorder());
        RobotArenaSettings.logger.setEditable(false);

        RobotArenaSettings.addLogMsg("Grid initialized:\nwidth:"+ RobotArenaSettings.getArenaWidth() +"  height:"+ RobotArenaSettings.getArenaHeight());
        
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

    basePanel.add(new JLabel("Name:"));
    basePanel.add(RobotArenaSettings.robotName);

    // Arena Size
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
