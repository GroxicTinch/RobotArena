package robotarena;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Control overall game
// Provide info to each virtual robot
// [FIXME] make stop actually stop the aiRandomMove

/**
 * A Swing GUI element that displays a grid on which you can draw images, text and lines.
 */
public class SwingArena extends JPanel {
  private double gridSquareSize; // Auto-calculated
  private int livingRobots;

  private static SwingArena instance;

  private ArrayList<Shot> robotShots;
  
  /**
   * Creates a new arena object
   */
  public SwingArena() {
    instance = this;
    robotShots = new ArrayList<Shot>();
  }

  public static SwingArena getInstance() { return instance; }

  // Starts and stops all threads related to the game progressing.
  public void start() {
    //RobotArenaSettings.logClear();
    RobotArenaSettings.log("-- Game Started");
    RobotControl robotControlBlank = new RobotControlImpl();
    RobotInfo[] robotInfoArray = robotControlBlank.getAllRobots();

    livingRobots = robotInfoArray.length;

    for(int i = 0; i < robotInfoArray.length; i++) {
      RobotInfo robot = robotInfoArray[i];

      robot.reset();

      RobotArenaSettings.log(robot.getName() +" has been added to the arena!" +
                           "\nWeighing in at "+ robot.getHealth() +" stamina" +
                           "\nStarting at X:"+ (robot.getX()+1) +" Y:"+ (robot.getY()+1) +
                           "\nWith the "+ robot.getAI().toString() +" brain\n");

      Runnable robotTask = () -> {
        try {
          RobotAI robotAI = robot.getAI();
          robotAI.runAI(robot.getControl());
        } catch (InterruptedException e) {

        }
      };

      robot.setAndStartThread(new Thread(robotTask, robot.getName() +"s thread"));
    }
  }

  public void stop() {
    RobotControl robotControlBlank = new RobotControlImpl();
    RobotInfo[] robotInfoArray = robotControlBlank.getAllRobots();
    for(int i = 0; i < robotInfoArray.length; i++) {
      robotInfoArray[i].stopThread();
    }

    livingRobots = 0;

    RobotArenaSettings.log("-- Game Stopped");

    repaint();
  }
  
  /**
   * Moves a robot image to a new grid position. This method is a *demonstration* of how you
   * can do such things, and you may want or need to modify it substantially.
   */
  public void setRobotPosition(RobotInfo robot, int x, int y) {
    // [FIXME] add thread protection
    if(robot != null) {
      robot.setPos(x, y);
    }
    repaint();
  }

  public void shoot(RobotInfo fromRobot, int x, int y) {
    // [TODO] add thread safety
    robotShots.add(new Shot(fromRobot.getX(), fromRobot.getY(), x, y));
    repaint();
    RobotInfo hurtRobot = (new RobotControlImpl()).isGridCellOccupied(x, y);
    if(hurtRobot != null) {
      boolean isDead = hurtRobot.damage(35);
      RobotArenaSettings.log(fromRobot.getName() +" shot "+ hurtRobot.getName() +" for 35 damage");

      if(isDead) {
        livingRobots--;
        hurtRobot.stopThread();
        RobotArenaSettings.log("The damage was fatal, killing "+ hurtRobot.getName());

        if(livingRobots <= 1) {
          RobotArenaSettings.log("All Other Robots are Dead, Game Over");
          stop();
        }
      }
    }
  }
  
  /**
   * This method is called in order to redraw the screen, either because the user is manipulating 
   * the window, OR because you've called 'repaint()'.
   *
   * You will need to modify the last part of this method; specifically the sequence of calls to
   * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D gfx = (Graphics2D) g;
    gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
               RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    
    // First, calculate how big each grid cell should be, in pixels. (We do need to do this
    // every time we repaint the arena, because the size can change.)
    gridSquareSize = Math.min(
      (double) getWidth() / (double) RobotArenaSettings.getArenaWidth(),
      (double) getHeight() / (double) RobotArenaSettings.getArenaHeight());
      
    int arenaPixelWidth = (int) ((double) RobotArenaSettings.getArenaWidth() * gridSquareSize);
    int arenaPixelHeight = (int) ((double) RobotArenaSettings.getArenaHeight() * gridSquareSize);
      
      
    // Draw the arena grid lines. This may help for debugging purposes, and just generally
    // to see what's going on.
    gfx.setColor(Color.GRAY);
    gfx.drawRect(0, 0, arenaPixelWidth - 1, arenaPixelHeight - 1); // Outer edge

    for(int gridX = 1; gridX < RobotArenaSettings.getArenaWidth(); gridX++) { // Internal vertical grid lines
      int x = (int) ((double) gridX * gridSquareSize);
      gfx.drawLine(x, 0, x, arenaPixelHeight);
    }
    
    for(int gridY = 1; gridY < RobotArenaSettings.getArenaHeight(); gridY++) { // Internal horizontal grid lines
      int y = (int) ((double) gridY * gridSquareSize);
      gfx.drawLine(0, y, arenaPixelWidth, y);
    }

    
    // Draw in Robots
    // Invoke helper methods to draw things at the current location.
    RobotControl robotControlBlank = new RobotControlImpl();
    RobotInfo[] robotInfoArray = robotControlBlank.getAllRobots();
    for(int i = 0; i < robotInfoArray.length; i++) {
      drawImage(gfx, robotInfoArray[i].getImage(), robotInfoArray[i].getX(), robotInfoArray[i].getY());
      drawLabel(gfx, robotInfoArray[i].getName() +" ("+ robotInfoArray[i].getHealth() +"%)", robotInfoArray[i].getX(), robotInfoArray[i].getY());
    }

    Iterator<Shot> iterator = robotShots.iterator();

    while(iterator.hasNext()) {
      Shot shot = iterator.next();
      if(shot != null && shot.stillAlive()) {
        drawLine(gfx, shot.x1, shot.y1, shot.x2, shot.y2);
      } else {
        iterator.remove();
      }
    }
  }
  
/*



    

  Modifying below this line should not be required.




 */
  
  /** 
   * Draw an image in a specific grid location. *Only* call this from within paintComponent(). 
   *
   * Note that the grid location can be fractional, so that (for instance), you can draw an image 
   * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
   *     
   * You shouldn't need to modify this method.
   */
  private void drawImage(Graphics2D gfx, ImageIcon icon, double gridX, double gridY) {
    // Get the pixel coordinates representing the centre of where the image is to be drawn. 
    double x = (gridX + 0.5) * gridSquareSize;
    double y = (gridY + 0.5) * gridSquareSize;
    
    // We also need to know how "big" to make the image. The image file has a natural width 
    // and height, but that's not necessarily the size we want to draw it on the screen. We 
    // do, however, want to preserve its aspect ratio.
    double fullSizePixelWidth = (double) icon.getIconWidth();
    double fullSizePixelHeight = (double) icon.getIconHeight();
    
    double displayedPixelWidth, displayedPixelHeight;
    if(fullSizePixelWidth > fullSizePixelHeight) {
      // Here, the image is wider than it is high, so we'll display it such that it's as 
      // wide as a full grid cell, and the height will be set to preserve the aspect 
      // ratio.
      displayedPixelWidth = gridSquareSize;
      displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
    } else {
      // Otherwise, it's the other way around -- full height, and width is set to 
      // preserve the aspect ratio.
      displayedPixelHeight = gridSquareSize;
      displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
    }

    // Actually put the image on the screen.
    gfx.drawImage(icon.getImage(), 
      (int) (x - displayedPixelWidth / 2.0),  // Top-left pixel coordinates.
      (int) (y - displayedPixelHeight / 2.0), 
      (int) displayedPixelWidth,              // Size of displayed image.
      (int) displayedPixelHeight, 
      null);
  }
  
  
  /**
   * Displays a string of text underneath a specific grid location. *Only* call this from within 
   * paintComponent(). 
   *
   * You shouldn't need to modify this method.
   */
  private void drawLabel(Graphics2D gfx, String label, double gridX, double gridY) {
    gfx.setColor(Color.BLUE);
    FontMetrics fm = gfx.getFontMetrics();
    gfx.drawString(label, 
      (int) ((gridX + 0.5) * gridSquareSize - (double) fm.stringWidth(label) / 2.0), 
      (int) ((gridY + 1.0) * gridSquareSize) + fm.getHeight());
  }
  
  /** 
   * Draws a (slightly clipped) line between two grid coordinates. 
   *
   * You shouldn't need to modify this method.
   */
  private void drawLine(Graphics2D gfx, double gridX1, double gridY1, 
                      double gridX2, double gridY2) {
    gfx.setColor(Color.RED);
    
    // Recalculate the starting coordinate to be one unit closer to the destination, so that it
    // doesn't overlap with any image appearing in the starting grid cell.
    final double radius = 0.5;
    double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
    double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
    double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
    
    gfx.drawLine((int) ((clippedGridX1 + 0.5) * gridSquareSize), 
           (int) ((clippedGridY1 + 0.5) * gridSquareSize), 
           (int) ((gridX2 + 0.5) * gridSquareSize), 
           (int) ((gridY2 + 0.5) * gridSquareSize));
  }
}
