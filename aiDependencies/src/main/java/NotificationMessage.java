package robotarena;

public class NotificationMessage {
  private RobotInfo robotInfo;
  private String msg;

  public NotificationMessage(RobotInfo robotInfoIn, String msgIn) {
    robotInfo = robotInfoIn;
    msg = msgIn;
  }

  public RobotInfo getRobot() {
    return robotInfo;
  }

  public String getMessage() {
    return msg;
  }
}