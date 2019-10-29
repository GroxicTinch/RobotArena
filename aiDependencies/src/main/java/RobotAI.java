package robotarena;

interface RobotAI {
  public final static boolean AIEXACTTIMEBETWEENMOVES = false;

  enum Direction { NORTH, SOUTH, EAST, WEST }

  public String toString();
  public void runAI(RobotControl robotControl) throws InterruptedException;
  public void sleepRand(long time) throws InterruptedException;
}
