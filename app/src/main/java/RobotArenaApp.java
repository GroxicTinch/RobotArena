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
      RobotInfo robot;

      // Settings need to be initialized before askRobotSettings is called
      RobotArenaSettings.initRobotControls();

      for(int i = 0; i < RobotArenaSettings.getRobotCount(); i++) {
        // If user cancels the GUI
        if(!askRobotSettings(i)) {
          userCanceled = true;
          break;
        } else {
          robot = new RobotInfoImpl(RobotArenaSettings.robotName.getText(),
                          (Integer)RobotArenaSettings.posXSpinner.getValue(),
                          (Integer)RobotArenaSettings.posYSpinner.getValue(),
                          (String)RobotArenaSettings.imageList.getSelectedItem(),
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

    // If user finished the settings then create the arena, else if canceled then print a message and exit
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

        // Create lambda button events
        btnStartGame.addActionListener((event) -> {
          btnStartGame.setEnabled(false);
          btnStopGame.setEnabled(true);

          arena.start();
        });

        btnStopGame.addActionListener((event) -> {
          btnStartGame.setEnabled(true);
          btnStopGame.setEnabled(false);

          arena.stop();
        });
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
    basePanel.setLayout(new GridLayout(2, 1, 5, 5));

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
    JPanel panelBase = new JPanel();
    GridBagLayout gbPanelBase = new GridBagLayout();
    GridBagConstraints gbcPanelBase = new GridBagConstraints();
    panelBase.setLayout(gbPanelBase);

    // Robot Name
    JPanel panelName = new JPanel();
    TitledBorder nameTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Name");
    panelName.setBorder(nameTitle);
    panelName.setLayout(new GridLayout(1, 1, 5, 5));

    panelName.add(RobotArenaSettings.robotName);

    // Name Set Constrain on base panel
    gridBagSetup(gbcPanelBase,
                 0,0,8,2,
                 GridBagConstraints.BOTH,
                 1,0,
                 GridBagConstraints.NORTH);
    gbPanelBase.setConstraints(panelName, gbcPanelBase);
    panelBase.add(panelName);

    // AI
    JPanel panelAiType = new JPanel();
    TitledBorder titleAIType = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "AI Type");
    panelAiType.setBorder(titleAIType);
    panelAiType.setLayout(new GridLayout(1, 1, 5, 5));
    panelAiType.add(RobotArenaSettings.aiList);

    // AI Set Constrain on base panel
    gridBagSetup(gbcPanelBase,
                 0,2,8,2,
                 GridBagConstraints.BOTH,
                 1,0,
                 GridBagConstraints.NORTH);
    gbPanelBase.setConstraints(panelAiType, gbcPanelBase);
    panelBase.add(panelAiType);

    // Start Position
    JPanel panelStartPos = new JPanel();
    TitledBorder titleStartPos = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                     , "Start Position");
    panelStartPos.setBorder(titleStartPos);
    GridBagLayout gbpanelStartPos = new GridBagLayout();
    GridBagConstraints gbcpanelStartPos = new GridBagConstraints();
    panelStartPos.setLayout(gbpanelStartPos);

    JLabel lblBlank = new JLabel();
    gridBagSetup(gbcpanelStartPos, 0,0,1,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(lblBlank, gbcpanelStartPos);
    panelStartPos.add(lblBlank);

    JLabel lblX = new JLabel("X:");
    gridBagSetup(gbcpanelStartPos, 1,0,1,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(lblX, gbcpanelStartPos);
    panelStartPos.add(lblX);

    gridBagSetup(gbcpanelStartPos, 2,0,2,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(RobotArenaSettings.posXSpinner, gbcpanelStartPos);
    panelStartPos.add(RobotArenaSettings.posXSpinner);

    lblBlank = new JLabel();
    gridBagSetup(gbcpanelStartPos, 4,0,1,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(lblBlank, gbcpanelStartPos);
    panelStartPos.add(lblBlank);

    JLabel lblY = new JLabel("Y:");
    gridBagSetup(gbcpanelStartPos, 5,0,1,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(lblY, gbcpanelStartPos);
    panelStartPos.add(lblY);

    gridBagSetup(gbcpanelStartPos, 6,0,2,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(RobotArenaSettings.posYSpinner, gbcpanelStartPos);
    panelStartPos.add(RobotArenaSettings.posYSpinner);

    lblBlank = new JLabel();
    gridBagSetup(gbcpanelStartPos, 8,0,1,1, GridBagConstraints.BOTH, 1,1, GridBagConstraints.NORTH);
    gbpanelStartPos.setConstraints(lblBlank, gbcpanelStartPos);
    panelStartPos.add(lblBlank);

    // Start Pos Set Constrain on base panel
    gridBagSetup(gbcPanelBase,
                 0,4,8,3,
                 GridBagConstraints.BOTH,
                 0,1,
                 GridBagConstraints.NORTH);
    gbPanelBase.setConstraints(panelStartPos, gbcPanelBase);
    panelBase.add(panelStartPos);

    // Robot Image
    JPanel panelImage = new JPanel();
    GridBagLayout gbPanelImage = new GridBagLayout();
    GridBagConstraints gbcPanelImage = new GridBagConstraints();
    panelImage.setLayout(gbPanelImage);

    gridBagSetup(gbcPanelImage,
                 0,0,1,4,
                 GridBagConstraints.BOTH,
                 1,1,
                 GridBagConstraints.NORTH);
    gbPanelImage.setConstraints(RobotArenaSettings.robotImage, gbcPanelImage);
    panelImage.add(RobotArenaSettings.robotImage);

    gridBagSetup(gbcPanelImage,
                 0,5,1,1,
                 GridBagConstraints.BOTH,
                 1,1,
                 GridBagConstraints.NORTH);
    gbPanelImage.setConstraints(RobotArenaSettings.imageList, gbcPanelImage);
    panelImage.add(RobotArenaSettings.imageList);

    // Image Set Constrain on base panel
    gridBagSetup(gbcPanelBase,
                 9,0,9,7,
                 GridBagConstraints.BOTH,
                 1,0,
                 GridBagConstraints.NORTH);
    gbPanelBase.setConstraints(panelImage, gbcPanelBase);
    panelBase.add(panelImage);

    return panelBase;
  }

  private static void gridBagSetup(GridBagConstraints gdc,
                            int gridX, int gridY, int gridW, int gridH,
                            int fill,
                            int weightX, int weightY,
                            int anchor) {
    gdc.gridx = gridX;
    gdc.gridy = gridY;
    gdc.gridwidth = gridW;
    gdc.gridheight = gridH;
    gdc.fill = fill;
    gdc.weightx = weightX;
    gdc.weighty = weightY;
    gdc.anchor = anchor;
  }
}
