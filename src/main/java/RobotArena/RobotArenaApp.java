import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.*;

public class RobotArenaApp {
    public static void main(String[] args) {
        // We want to ask the user before anything is initialized so do it before invoke.
        RobotArenaSettings robotArenaSettings = askSettings();

        if(robotArenaSettings != null) {
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
                
                JTextArea logger = new JTextArea();
                JScrollPane loggerArea = new JScrollPane(logger);
                loggerArea.setBorder(BorderFactory.createEtchedBorder());
                logger.append("Hello\n");
                logger.append("World\n");
                
                JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT, arena, logger);

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

    private static RobotArenaSettings askSettings() {
        RobotArenaSettings robotArenaSettings = new RobotArenaSettings();

        int selection = JOptionPane.showConfirmDialog(null, settingsPanel(robotArenaSettings), "Robot Arena Settings"
                                                      , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (selection == JOptionPane.OK_OPTION) {
            try {
                robotArenaSettings.widthSpinner.commitEdit();
                robotArenaSettings.heightSpinner.commitEdit();
                robotArenaSettings.robotAmountSpinner.commitEdit();
            } catch(ParseException ex) {
                // Do nothing
            }
        } else /*if (selection == JOptionPane.CANCEL_OPTION)*/ {
            robotArenaSettings = null;
        }

        return robotArenaSettings;
    }

    private static JPanel settingsPanel(RobotArenaSettings robotArenaSettings) {
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new GridLayout(3, 1, 5, 5));

        // Arena Size
        JPanel arenaSizePanel = new JPanel();
        TitledBorder arenaSizeTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                                                       , "Arena Size");
        arenaSizePanel.setBorder(arenaSizeTitle);
        arenaSizePanel.setLayout(new GridLayout(1, 5, 5, 5));

        arenaSizePanel.add(new JLabel("Width:"));
        arenaSizePanel.add(robotArenaSettings.widthSpinner);
        arenaSizePanel.add(new JLabel()); // Blank cell

        arenaSizePanel.add(new JLabel("Height:"));
        arenaSizePanel.add(robotArenaSettings.heightSpinner);
        basePanel.add(arenaSizePanel);

        // Robot Amount
        JPanel robotAmountPanel = new JPanel();
        TitledBorder robotTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                                                                       , "Robots");
        robotAmountPanel.setBorder(robotTitle);
        robotAmountPanel.setLayout(new GridLayout(1, 5, 5, 5));

        robotAmountPanel.add(new JLabel("Amount:"));
        robotAmountPanel.add(robotArenaSettings.robotAmountSpinner);
        robotAmountPanel.add(new JLabel()); // Blank cell
        robotAmountPanel.add(new JLabel()); // Blank cell
        robotAmountPanel.add(new JLabel()); // Blank cell

        basePanel.add(robotAmountPanel);

        return basePanel;
    }
}
