import javax.swing.*;

public class RobotArenaSettings {
    // Limits
    final static int MINWIDTH = 8;
    final static int MAXWIDTH = 15;

    final static int MINHEIGHT = 8;
    final static int MAXHEIGHT = 15;

    final static int MAXROBOTS = 4; // [TODO] find a better number

    // Current Values
    final static int DEFAULTWIDTH = 12;
    final static int DEFAULTHEIGHT = 8;

    // JPanel Controls
    // SpinnerNumberModel(defaultValue, min, max, step);
    public JSpinner widthSpinner;
    public JSpinner heightSpinner;

    public JSpinner robotAmountSpinner;

    // [TODO] AI Type for each robot
    // [TODO] Starting Loc
    // [TODO] Robot Name

    public RobotArenaSettings() {
        widthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTWIDTH, MINWIDTH, MAXWIDTH, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(DEFAULTHEIGHT, MINHEIGHT, MAXHEIGHT, 1));

        robotAmountSpinner = new JSpinner(new SpinnerNumberModel(2, 2, MAXROBOTS, 1));
    }

    // Get Values
    public int getArenaWidth() {
        return (Integer)widthSpinner.getValue();
    }

    public int getArenaHeight() {
        return (Integer)heightSpinner.getValue();
    }

    public int getRobotCount() {
        return (Integer)robotAmountSpinner.getValue();
    }
}
