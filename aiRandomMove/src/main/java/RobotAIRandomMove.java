package robotarena;

public class RobotAIRandomMove implements RobotAI {
  static {//[FIXME] Need to make project auto build native code
    try {
      System.loadLibrary("aiRandomMove");
    } catch (UnsatisfiedLinkError e) {
      System.out.println("aiRandomMove library failed to load.\n" + e);
      System.exit(1);
    }
  }

  public RobotAIRandomMove() {}
  
  @Override
  public String toString() {
    return "Random Move Direction";
  }

  public native void runAI(RobotControl robotControl) throws InterruptedException;
}