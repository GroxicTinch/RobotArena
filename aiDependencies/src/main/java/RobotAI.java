package robotarena;

interface RobotAI {
  enum Direction { NORTH, SOUTH, EAST, WEST }

  public String toString();
  public void runAI(RobotControl robotControl) throws InterruptedException;
}
