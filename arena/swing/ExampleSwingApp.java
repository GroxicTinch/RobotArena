import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ExampleSwingApp 
{
    public static void main(String[] args) 
    {
        // Note: SwingUtilities.invokeLater() is equivalent to JavaFX's Platform.runLater().
        SwingUtilities.invokeLater(() ->
        {
            JFrame window = new JFrame("Robot AI Test (Swing)");
            SwingArena arena = new SwingArena();
            
            JToolBar toolbar = new JToolBar();
            JButton btn1 = new JButton("My Button 1");
            JButton btn2 = new JButton("My Button 2");
            toolbar.add(btn1);
            toolbar.add(btn2);
            
            btn1.addActionListener((event) ->
            {
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
    }    
}
