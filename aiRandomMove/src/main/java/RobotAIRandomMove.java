package robotarena;

public class RobotAIRandomMove extends RobotAIDefault {
  static {
    try {
      // If the current computer is a 64bit then load the 64bit .dll else load the 32bit
      String nativeLibPath;
      switch (System.getProperty("sun.arch.data.model")){
        case "64":
          nativeLibPath = "x64";
          break;
        // case "32":
        default:
          nativeLibPath = "x86";
          break;
      }

      nativeLibPath += "/aiRandomMove";

      System.loadLibrary(nativeLibPath);

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